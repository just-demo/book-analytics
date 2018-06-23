package edu.self.web.controller;

import com.google.gson.Gson;
import com.sun.jersey.multipart.FormDataParam;
import edu.self.services.dicts12.GroupService;
import edu.self.services.google.translate.GoogleTranslate;
import edu.self.services.google.translate.GoogleTranslateLocal;
import edu.self.services.text.TextAnalyzer;
import edu.self.services.text.TextAnalyzerStrict;
import edu.self.services.user.UserWordsService;
import edu.self.utils.CustomFileUtils;
import edu.self.utils.TextUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;

import static edu.self.utils.Config.CONFIG_ROOT;

@Path("/book")
public class BookController {
    //@Context
    //UriInfo uriInfo;
    //@Context
    //Request request;

    public static void main(String[] args) throws JAXBException, IOException {
        String text = FileUtils.readFileToString(new File(CONFIG_ROOT + "\\book\\the_secret_adv.txt"));
        List<List<Object[]>> groupPrepared = new BookController().prepareGroups(text);
        Gson gson = new Gson();
        String gsonString = gson.toJson(groupPrepared);
        System.out.println(gsonString);
        //234.359375
        //993.2548828125
    }

    private TextAnalyzer textAnalyzer;
    private GroupService groupService;
    private GoogleTranslate googleTranslate;
    private UserWordsService userWordsService;

    private Set<String> managedWords;

    public BookController() throws JAXBException, IOException {
        String dicts12File = CONFIG_ROOT + "\\12dicts_groups.xml";
        String googleFile = CONFIG_ROOT + "\\google\\translations.xml";
        String wordsFile = CONFIG_ROOT + "\\words.txt";
        String userWordsFile = CONFIG_ROOT + "\\user\\words.xml";

        managedWords = TextUtils.getWords(FileUtils.readFileToString(new File(wordsFile)));
        groupService = new GroupService(dicts12File);
        userWordsService = new UserWordsService(userWordsFile);
        textAnalyzer = new TextAnalyzerStrict(managedWords);
        googleTranslate = new GoogleTranslateLocal(googleFile);
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
    public Response parseFile(@FormDataParam("file") InputStream file) throws IOException, ServletException {
        String text = CustomFileUtils.read(file);
        List<List<Object[]>> groupPrepared = prepareGroups(text);
        Gson gson = new Gson();
        String gsonString = gson.toJson(groupPrepared);
        //Viewable does not work locally
        //Viewable template = new Viewable("/WEB-INF/jsp/book_result.jsp", gsonString);
        String template = getResultPage(gsonString);
        return Response.status(200).entity(template).build();
    }

    private List<List<Object[]>> prepareGroups(String text) {
        Map<String, Integer> wordOccurrencies = textAnalyzer.getWordOccurrencies(text);
        Collection<Collection<String>> groups = groupService.group(wordOccurrencies.keySet());
        // wrap and sort
        List<SortWrapper<List<SortWrapper<String>>>> groupWrappers = new ArrayList<SortWrapper<List<SortWrapper<String>>>>();
        for (Collection<String> group : groups) {
            int total = 0;
            List<SortWrapper<String>> wordWrappers = new ArrayList<SortWrapper<String>>();
            for (String word : group) {
                Integer count = wordOccurrencies.get(word);
                wordWrappers.add(new SortWrapper<String>(word, count));
                total += count;
            }
            Collections.sort(wordWrappers);
            groupWrappers.add(new SortWrapper<List<SortWrapper<String>>>(wordWrappers, total));
        }
        Collections.sort(groupWrappers);
        // unwrap and fill data
        List<List<Object[]>> groupPrepared = new ArrayList<List<Object[]>>();
        for (SortWrapper<List<SortWrapper<String>>> groupWrapper : groupWrappers) {
            List<Object[]> group = new ArrayList<Object[]>();
            for (SortWrapper<String> wordWrapper : groupWrapper.value) {
                String word = wordWrapper.value;
                String translation = userWordsService.getTranslation(word);
                boolean isTranslationSaved = true;
                if (translation == null) {
                    translation = googleTranslate.translate(word);
                    isTranslationSaved = false;
                }
                group.add(new Object[]{
                        word, // text
                        translation, // translation
                        wordOccurrencies.get(word), // occurancy
                        isTranslationSaved ? 1 : 0, // is translation saved
                        userWordsService.isIgnorable(word) ? 1 : 0, // ignorable
                        managedWords.contains(word) ? 1 : 0, // is managed
                        googleTranslate.getTranslations(word), //all translations
                });
            }
            groupPrepared.add(group);
        }
        return groupPrepared;
    }

    private class SortWrapper<T> implements Comparable<SortWrapper<T>> {
        private T value;
        private Integer count;

        public SortWrapper(T value, Integer count) {
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
        Map<String, Integer> wordOccurrencies = textAnalyzer.getWordOccurrencies(text);
        Map<String, List<String>> translations = new HashMap<String, List<String>>();
        for (String word : wordOccurrencies.keySet()) {
            translations.put(word.toLowerCase(), googleTranslate.getTranslations(word));
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
                "    var translations = " + new Gson().toJson(translations) + ";" +
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
        List<String> words = new ArrayList<String>(wordsSet);
        Collections.sort(words, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.length() - o1.length();
            }
        });

        for (int i = words.size() - 1; i > -1; i--) {
            words.set(i, Pattern.quote(words.get(i)));
        }

        return "(" + StringUtils.join(words, "|") + ")";
    }

    @GET
    @Path("/test")
    @Produces("text/plain")
    public String test() {
        String output = "Book Test";
        return output;
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
