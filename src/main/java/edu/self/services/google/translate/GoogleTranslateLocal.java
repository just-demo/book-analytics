package edu.self.services.google.translate;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import edu.self.model.google.GoogleTranslations;
import edu.self.utils.XMLUtils;

public class GoogleTranslateLocal implements GoogleTranslate {
    Map<String, List<String>> translations;

    public GoogleTranslateLocal(String fileName) throws FileNotFoundException, JAXBException {
        GoogleTranslations googleTranslations = XMLUtils.load(fileName, GoogleTranslations.class);
        translations = new HashMap<String, List<String>>();
        for (GoogleTranslations.Translation translation : googleTranslations.getTranslations()) {
            translations.put(translation.getText(), translation.getTranslation());
        }
    }

    @Override
    public String translate(String text) {
        List<String> translation = translations.get(text.trim());
        return translation != null && !translation.isEmpty() ? translation.get(0) : "";
    }

    @Override
    public List<String> getTranslations(String text) {
        List<String> translation = translations.get(text.trim());
        if (translation == null) {
            translation = new ArrayList<String>();
        }
        return translation;
    }
}
