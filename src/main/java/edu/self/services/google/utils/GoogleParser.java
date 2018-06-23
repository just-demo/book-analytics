package edu.self.services.google.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.bind.JAXBException;

import edu.self.model.google.GoogleQueries;
import edu.self.model.google.GoogleTranslations;
import edu.self.services.google.GoogleUtils;
import edu.self.utils.Log;
import edu.self.utils.XMLUtils;

import static edu.self.utils.Config.CONFIG_ROOT;

public class GoogleParser {
    public static void main(String[] args) throws Exception {
        new GoogleParser().parse(
                CONFIG_ROOT + "\\google\\queries.xml",
                CONFIG_ROOT + "\\google\\translations.xml"
        );
    }

    public void parse(String queriesInputFile, String translationsOutputFile) throws FileNotFoundException, IOException, JAXBException {
        Date start = new Date();

        GoogleQueries googleQueries = XMLUtils.load(queriesInputFile, GoogleQueries.class);
        GoogleTranslations googleTranslations = new GoogleTranslations();
        int i = 0;
        int success = 0;
        for (GoogleQueries.Query query : googleQueries.getQueries()) {
            ++i;
            String word = query.getText();
            String response = query.getResponce();
            String translation = GoogleUtils.fetchTranslation(response);
            if (translation.equalsIgnoreCase(word)) {
                translation = null;
            }
            if (translation != null) {
                Set<String> translations = new LinkedHashSet<String>();
                translations.add(translation);
                translations.addAll(GoogleUtils.fetchTranslations(response));
                googleTranslations.getTranslations().add(new GoogleTranslations.Translation(word, new ArrayList<String>(translations)));
                ++success;
            }

            if (i % 10000 == 0) {
                System.out.println("s/i=" + success + "/" + i);
                Log.time(start);
            }
        }

        XMLUtils.save(translationsOutputFile, googleTranslations);
        System.out.println("s/i=" + success + "/" + i);
        Log.time(start);
    }
}
