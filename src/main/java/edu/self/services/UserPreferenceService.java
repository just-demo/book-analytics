package edu.self.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.NotImplementedException;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;

import static edu.self.utils.FileUtils.readResourceFile;

public class UserPreferenceService {
    private Map<String, Boolean> ignorable;
    private Map<String, String> translations;

    public UserPreferenceService() {
        try {
            ignorable = new ObjectMapper().readValue(readResourceFile("user-ignorable.json"), new TypeReference<Map<String, Boolean>>() {
            });
            translations = new ObjectMapper().readValue(readResourceFile("user-translations.json"), new TypeReference<Map<String, String>>() {
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean isIgnorable(String text) {
        return ignorable.getOrDefault(text, false);
    }

    public String getTranslation(String text) {
        return translations.get(text);
    }

    public void setTranslation(String text, String translation) {
        // TODO: implement
        throw new NotImplementedException();
    }

    public void setIgnorable(String text, boolean ignorable) {
        // TODO: implement
        throw new NotImplementedException();
    }
}
