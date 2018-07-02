package edu.self.rest;

import edu.self.dto.Statistics;
import edu.self.dto.StatisticsGroup;
import edu.self.services.GroupService;
import edu.self.services.TranslationService;
import edu.self.services.UserPreferenceService;
import edu.self.services.text.TextAnalyzer;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static edu.self.utils.JsonUtils.toJson;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Comparator.comparing;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.stream.Collectors.*;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.cors.CorsConfiguration.ALL;

@CrossOrigin(ALL)
@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private TextAnalyzer textAnalyzer;

    @Autowired
    private GroupService groupService;

    @Autowired
    private TranslationService translationService;

    @Autowired
    private UserPreferenceService userPreferenceService;

    @Autowired
    private Set<String> words;

    @GetMapping(path = "", produces = TEXT_HTML_VALUE)
    public String bookInput() {
        return getInputPage();
    }

    @PostMapping("/upload")
    public Object uploadBook(@RequestParam("file") MultipartFile file) throws IOException {
        String text = new String(file.getBytes(), UTF_8);
        return prepareGroups(text);
    }

    @PostMapping(path = "/textToBook", produces = TEXT_HTML_VALUE)
    public String textToBook(@RequestParam("text") String text) {
        Map<String, Long> wordOccurrences = textAnalyzer.getWordOccurrences(text);
        Map<String, List<String>> translations = wordOccurrences.keySet().stream()
                .collect(toMap(String::toLowerCase, translationService::getTranslations));
        String wordsPattern = makePattern(translations.keySet());
        String textPrepared = Pattern.compile(wordsPattern, CASE_INSENSITIVE).matcher(text).replaceAll("<span>$1</span>")
                .replaceAll("( )(?= )", "&nbsp;")
                .replaceAll("\\n", "<br/>\n");
        return gettextToBookResultPage(textPrepared, translations);
    }

    @GetMapping(path = "/test", produces = TEXT_PLAIN_VALUE)
    public String test() {
        return "Book Test";
    }

    private String makePattern(Set<String> words) {
        return "(" + words.stream()
                .sorted(comparing(String::length).reversed())
                .map(Pattern::quote)
                .collect(joining("|")) + ")";
    }

    private List<List<Pair<String, Long>>> prepareGroups(String text) {
        Map<String, Long> wordOccurrences = textAnalyzer.getWordOccurrences(text);
        Collection<Collection<String>> groups = groupService.group(wordOccurrences.keySet());

        return groups.stream()
                .map(group -> group.stream()
                        .map(word -> Pair.of(word, wordOccurrences.get(word)))
                        .sorted(comparing(this::getOccurrence).reversed())
                        .collect(toList())
                ).sorted(comparing(this::sumOccurrence).reversed()).collect(toList());
    }

    private long getOccurrence(Pair<String, Long> item) {
        return item.getValue();
    }

    private long sumOccurrence(Collection<Pair<String, Long>> items) {
        return items.stream()
                .mapToLong(Pair::getValue)
                .sum();
    }

    private String gettextToBookResultPage(String textPrepared, Map<String, List<String>> translations) {
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" +
                "<html>" +
                "<head>" +
                "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                "  <title></title>" +
                "  <style type='text/css'>" +
                "    #translation {" +
                "      position: fixed;" +
                "      top: 0px;" +
                "      margin: 10px;" +
                "      padding: 10px;" +
                "      font-family: monospace;" +
                "      border: 1px solid #AAAAAA;" +
                "      display: none;" +
                "    }" +
                "    #text {" +
                "      display: inline-block;" +
                "      font-family: monospace;" +
                "    }" +
                "    span:hover {" + "" +
                "      background-color: #DDDDDD;" +
                "      cursor: pointer;" +
                "    }" +
                "  </style>" +
                "  <script type='text/javascript'>" +
                "    var translations = " + toJson(translations) + ";" +
                "    function translate(){" +
                "      var word = this.innerHTML;" +
                "      var wordTranslations = translations[word.toLowerCase()] || [];" +
                "      var translationItems = [];" +
                "      translationItems.push('<div>', '<b>', word, ':</b>', '</div>');" +
                "      for (var i = 0; i < wordTranslations.length; ++i) {" +
                "        if (wordTranslations[i]) {" +
                "          translationItems.push('<div>', wordTranslations[i], '</div>');" +
                "        }" +
                "      }\n" +
                "      var translation = document.getElementById('translation');" +
                "      translation.innerHTML = translationItems.join('');" +
                "      translation.style.display = 'block';" +
                "    }\n" +
                "    function hideTranslation(){" +
                "      var translation = document.getElementById('translation');" +
                "      translation.style.display = 'none';" +
                "    }\n" +
                "    function init(){" +
                "      var translation = document.getElementById('translation');" +
                "      translation.style.left = document.getElementById('text').offsetWidth + 'px';" +
                //"      translation.onclick = hideTranslation;" +
                //"      document.onclick = hideTranslation;" +
                "      var words = document.getElementsByTagName('span');" +
                "      for (var i in words){" +
                "        words[i].onclick = translate;" +
                //"        words[i].onmouseover = translate;" +
                "        words[i].onmouseout = hideTranslation;" +
                "      }" +
                "    }" +
                "  </script>" +
                "</head>" +
                "<body onload='init();'>" +
                "  <div id='text'>" + textPrepared + "</div>" +
                "  <div id='translation'></div>" +
                "</body>" +
                "</html>";
    }

    private String getInputPage() {
        return getPage(
                "<form action=\"\" method=\"POST\" enctype=\"multipart/form-data\">" +
                        "<input type=\"file\" name=\"file\" onchange=\"this.parentNode.submit();\" />" +
                        "</form>"
        );
    }

    private String getResultPage(String result) {
        return getPage(
                "<script type=\"text/javascript\">" +
                        "window.parent.postUpload(" + result + ");" +
                        "</script>"
        );
    }

    private String getPage(String body) {
        return
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" +
                        "<html>" +
                        "<head>" +
                        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
                        "<title></title>" +
                        "</head>" +
                        "<body>" +
                        body +
                        "</body>" +
                        "</html>";
    }
}
