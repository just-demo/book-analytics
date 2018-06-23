package edu.self.services.google.translate;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edu.self.services.google.GoogleAPI;
import edu.self.services.google.GoogleUtils;

public class GoogleTranslateRemote implements GoogleTranslate {
    private GoogleAPI googleAPI;

    public GoogleTranslateRemote() {
        googleAPI = new GoogleAPI();
    }

    @Override
    public String translate(String text) {
        text = text.trim();
        String translation = "";
        if (!text.isEmpty()) {
            String googleResponseJson = googleAPI.query(text);
            translation = GoogleUtils.fetchTranslation(googleResponseJson);
        }
        return translation;
    }

    @Override
    public List<String> getTranslations(String text) {
        text = text.trim();
        if (!text.isEmpty()) {
            String googleResponseJson = googleAPI.query(text);
            Set<String> translations = new LinkedHashSet<String>();
            translations.add(GoogleUtils.fetchTranslation(googleResponseJson));
            translations.addAll(GoogleUtils.fetchTranslations(googleResponseJson));
            return new ArrayList<String>(translations);
        }
        return new ArrayList<String>();
    }
}
