package edu.self.web;

import edu.self.services.GroupService;
import edu.self.services.TranslationService;
import edu.self.services.UserPreferenceService;
import edu.self.services.text.TextAnalyzer;
import edu.self.services.text.TextAnalyzerStrict;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;

import static edu.self.utils.FileUtils.readResourceFile;
import static edu.self.utils.JsonUtils.toJson;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

@Singleton
@Path("/book")
public class BookController {

    private TextAnalyzer textAnalyzer;
    private GroupService groupService;
    private TranslationService translationService;
    private UserPreferenceService userPreferenceService;

    private Set<String> managedWords;

    public BookController() throws IOException {
        managedWords = stream(readResourceFile("words.txt").split("\n"))
                .filter(StringUtils::isNotEmpty)
                .collect(toSet());
        groupService = new GroupService();
        userPreferenceService = new UserPreferenceService();
        textAnalyzer = new TextAnalyzerStrict(managedWords);
        translationService = new TranslationService();
    }

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public Response bookInput() {
        //Viewable does not work locally
        //Viewable template = new Viewable("/WEB-INF/jsp/book_input.jsp");
        String template = getInputPage();
        return Response.status(200).entity(template).build();
    }

    @POST
    @Path("/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    // @Produces(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_HTML)
    public Response parseFile(@FormDataParam("file") InputStream file) throws IOException {
        String text = IOUtils.toString(file, UTF_8);
        List<List<Object[]>> groupPrepared = prepareGroups(text);
        String jsonString = toJson(groupPrepared);
        //Viewable does not work locally
        //Viewable template = new Viewable("/WEB-INF/jsp/book_result.jsp", jsonString);
        String template = getResultPage(jsonString);
        return Response.status(200).entity(template).build();
    }

    private List<List<Object[]>> prepareGroups(String text) {
        Map<String, Integer> wordOccurrences = textAnalyzer.getWordOccurrences(text);
        Collection<Collection<String>> groups = groupService.group(wordOccurrences.keySet());
        // wrap and sort
        List<SortWrapper<List<SortWrapper<String>>>> groupWrappers = new ArrayList<>();
        for (Collection<String> group : groups) {
            int total = 0;
            List<SortWrapper<String>> wordWrappers = new ArrayList<>();
            for (String word : group) {
                Integer count = wordOccurrences.get(word);
                wordWrappers.add(new SortWrapper<>(word, count));
                total += count;
            }
            Collections.sort(wordWrappers);
            groupWrappers.add(new SortWrapper<>(wordWrappers, total));
        }
        Collections.sort(groupWrappers);
        // unwrap and fill data
        List<List<Object[]>> groupPrepared = new ArrayList<>();
        for (SortWrapper<List<SortWrapper<String>>> groupWrapper : groupWrappers) {
            List<Object[]> group = new ArrayList<>();
            for (SortWrapper<String> wordWrapper : groupWrapper.value) {
                String word = wordWrapper.value;
                String translation = userPreferenceService.getTranslation(word);
                boolean isTranslationSaved = true;
                if (translation == null) {
                    translation = translationService.translate(word);
                    isTranslationSaved = false;
                }
                group.add(new Object[]{
                        word, // text
                        translation, // translation
                        wordOccurrences.get(word), // occurrence
                        isTranslationSaved ? 1 : 0, // is translation saved
                        userPreferenceService.isIgnorable(word) ? 1 : 0, // ignorable
                        managedWords.contains(word) ? 1 : 0, // is managed
                        translationService.getTranslations(word), //all translations
                });
            }
            groupPrepared.add(group);
        }
        return groupPrepared;
    }

    private class SortWrapper<T> implements Comparable<SortWrapper<T>> {
        private T value;
        private Integer count;

        SortWrapper(T value, Integer count) {
            this.value = value;
            this.count = count;
        }

        @Override
        public int compareTo(SortWrapper<T> o) {
            return -count.compareTo(o.count);
        }
    }

    @POST
    @Path("/textToBook")
    @Produces("text/html")
    public String textToBook(@FormParam("text") String text) {
        Map<String, Integer> wordOccurrencies = textAnalyzer.getWordOccurrences(text);
        Map<String, List<String>> translations = new HashMap<>();
        for (String word : wordOccurrencies.keySet()) {
            translations.put(word.toLowerCase(), translationService.getTranslations(word));
        }

        String wordsPattern = makePattern(translations.keySet());
        System.out.println(wordsPattern);
        text = Pattern.compile(wordsPattern, Pattern.CASE_INSENSITIVE).matcher(text).replaceAll("<span>$1</span>");

        text = text /*.replaceAll(wordsPattern, "<span>$1</span>")*/.replaceAll("( )(?= )", "&nbsp;").replaceAll("\\n", "<br/>\n");
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
                "  <div id='text'>" + text + "</div>" +
                "  <div id='translation'></div>" +
                "</body>" +
                "</html>";
    }

    private String makePattern(Set<String> wordsSet) {
        List<String> words = new ArrayList<>(wordsSet);
        words.sort((o1, o2) -> o2.length() - o1.length());

        for (int i = words.size() - 1; i > -1; i--) {
            words.set(i, Pattern.quote(words.get(i)));
        }

        return "(" + StringUtils.join(words, "|") + ")";
    }

    @GET
    @Path("/test")
    @Produces("text/plain")
    public String test() {
        return "Book Test";
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
                        //in jsp template it would be window.parent.postUpload(${it});
                        //additionally <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> directive would be added
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
