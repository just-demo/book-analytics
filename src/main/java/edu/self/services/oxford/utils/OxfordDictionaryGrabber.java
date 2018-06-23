package edu.self.services.oxford.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import edu.self.utils.Log;
import edu.self.utils.TextUtils;
import edu.self.utils.WebUtils;

import static edu.self.utils.Config.CONFIG_ROOT;

public class OxfordDictionaryGrabber {
    public static void main(String[] args) throws Exception {
        new OxfordDictionaryGrabber().grabAll(CONFIG_ROOT + "\\web2.txt", CONFIG_ROOT + "\\oxford\\out");
    }

    public void grabAll(String inputFile, String outputFolder) throws IOException {
        Date start = new Date();
        OxfordDictionaryGrabber oxford = new OxfordDictionaryGrabber();
        String text = FileUtils.readFileToString(new File(inputFile));
        Set<String> words = TextUtils.getWords(text);
        int i = 0;
        Properties results = new Properties();
        int success = 0;
        for (String word : words) {
            ++i;
            String outputFile = outputFolder + "\\" + OxfordUtils.getFileSubPath(i) + "\\" + word + ".xml";
            boolean result = false;

            try {
                String page = oxford.query(word);
                page = page.replaceAll("\\s+", " ").replaceAll("\\s*(<|>)\\s*", "$1");
                FileUtils.writeStringToFile(new File(outputFile), page);
                result = true;
            } catch (Exception e) { //just not to stop file processing
                // TODO: handle exception
            }
            results.put(word, String.valueOf(result));
            if (result) {
                ++success;
            }
            if (i % 10 == 0) {
                if (i % 1000 == 0) {
                    results.store(new FileOutputStream(outputFolder + "\\results.properties"), null);
                    if (i % 10000 == 0) {
                        results.store(new FileOutputStream(outputFolder + "\\results_" + i + ".properties"), null);
                    }
                }
                System.out.println("s/i=" + success + "/" + i);
                Log.time(start);
            }
        }
        results.store(new FileOutputStream(outputFolder + "\\results.properties"), null);
        System.out.println("s/n=" + success + "/" + words.size());
        Log.time(start);
    }

    private String query(String text) {
        String path;
        try {
            path = "http://oxforddictionaries.com/definition/" + URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String result = WebUtils.load(path);
        return result;
    }
}
