package edu.self.services.oxford.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;

import edu.self.model.oxford.OxfordWords;
import edu.self.utils.CollectionUtils;
import edu.self.utils.CustomFileUtils;
import edu.self.utils.Log;
import edu.self.utils.TextUtils;
import edu.self.utils.XMLUtils;

import static edu.self.utils.Config.CONFIG_ROOT;

public class OxfordDictionaryParser {
    private Pattern preDerivativesPattern = Pattern.compile("<h2>Derivatives</h2><dl>(.*?)</dl>");
    //	TODO: see also <span class="partOfSpeech">noun</span>
    private Pattern derivativesPattern = Pattern.compile("<h3>(.*?)</h3>");
    private Pattern inflectionPattern = Pattern.compile("<span class=\"inflection\">(.*?)</span>");

    public static void main(String[] args) throws Exception {
        new OxfordDictionaryParser().parse(
                CONFIG_ROOT + "\\web2.txt",
                CONFIG_ROOT + "\\oxford\\out\\results.properties",
                CONFIG_ROOT + "\\oxford\\out",
                CONFIG_ROOT + "\\oxford\\out\\oxford_parsed.xml"
        );
    }

    public void parse(String allWordsFile, String processedWordsFile, String pagesFolder, String outFile) throws IOException, JAXBException, InterruptedException {
        Date start = new Date();

        String text = CustomFileUtils.read(allWordsFile);
        Set<String> words = TextUtils.getWords(text);

        int i = 0;
        OxfordWords oxfordWords = new OxfordWords();
        Set<String> allOxfordWords = new HashSet<String>();
        Properties results = new Properties();
        results.load(new FileInputStream(processedWordsFile));
        for (String word : words) {
            ++i;
            if (results.containsKey(word) && Boolean.valueOf((String) results.get(word))) {
                String fileName = pagesFolder + "\\" + OxfordUtils.getFileSubPath(i) + "\\" + word + ".xml";
                //System.out.println("file=" + fileName);
                //logMemory();

                String page = FileUtils.readFileToString(new File(fileName));
                Set<String> inflections = fetchInflections(page);
                Set<String> derivatives = fetchDerivatives(page);
                oxfordWords.getWords().add(new OxfordWords.Word(word, inflections, derivatives));
                allOxfordWords.add(word);
                allOxfordWords.addAll(inflections);
                allOxfordWords.addAll(derivatives);
            }
            if (i % 1000 == 0) {
                System.out.println(i);
            }
        }

        XMLUtils.save(outFile, oxfordWords);
        System.out.println("oxford/oxford+/total=" + oxfordWords.getWords().size() + "/" + allOxfordWords.size() + "/" + words.size());
        Log.time(start);

        System.out.println("total - oxford=" + CollectionUtils.diff(words, allOxfordWords).size());
        System.out.println("oxford - total=" + CollectionUtils.diff(allOxfordWords, words).size());

        Log.memory();
        Log.time(start);
    }

    //substring leave reference to original string that leads to memory leaks
    private String copy(String origin) {
        return new String(origin.toCharArray());
    }

    private Set<String> fetchInflections(String text) throws InterruptedException {
        Set<String> inflections = new HashSet<String>();
        Matcher matcher = inflectionPattern.matcher(text);
        while (matcher.find()) {
            inflections.add(copy(matcher.group(1).trim()));
        }
        return inflections;
    }

    private Set<String> fetchDerivatives(String text) {
        Set<String> derivatives = new HashSet<String>();
        Matcher preMatcher = preDerivativesPattern.matcher(text);
        while (preMatcher.find()) {
            String container = preMatcher.group(1);
            Matcher matcher = derivativesPattern.matcher(container);
            while (matcher.find()) {
                derivatives.add(copy(matcher.group(1).trim()));
            }
        }
        return derivatives;
    }
}
