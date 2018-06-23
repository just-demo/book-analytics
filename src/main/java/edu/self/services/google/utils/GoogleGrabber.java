package edu.self.services.google.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;

import edu.self.model.google.GoogleQueries;
import edu.self.services.google.GoogleAPI;
import edu.self.utils.Log;
import edu.self.utils.TextUtils;
import edu.self.utils.XMLUtils;

import static edu.self.utils.Config.CONFIG_ROOT;

public class GoogleGrabber {
    public static void main(String[] args) throws Exception {
        new GoogleGrabber().grabAll(CONFIG_ROOT + "\\words.txt", CONFIG_ROOT + "\\google\\queries.xml");
    }

    public void grabAll(String inputFile, String queriesOutputFile) throws FileNotFoundException, IOException, JAXBException {
        Date start = new Date();

        GoogleAPI google = new GoogleAPI();
        String text = FileUtils.readFileToString(new File(inputFile));
        Set<String> words = TextUtils.getWords(text);

        GoogleQueries googleQueries = new GoogleQueries();
        int i = 0;
        for (String word : words) {
            ++i;
            String response = google.query(word);
            googleQueries.getQueries().add(new GoogleQueries.Query(word, response));
            if (i % 1000 == 0) {
                System.out.println(i);
                Log.time(start);
            }
        }

        XMLUtils.save(queriesOutputFile, googleQueries);
        System.out.println(i);
        Log.time(start);
    }
}
