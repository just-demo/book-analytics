package self.edu.services;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.util.Map;

import static self.edu.utils.FileUtils.readResourceFile;
import static self.edu.utils.JsonUtils.fromJson;

@Service
public class UserPreferenceService {
    private Map<String, Boolean> ignorable;
    private Map<String, String> translations;

    public UserPreferenceService() {
        ignorable = fromJson(readResourceFile("user-ignorable.json"), new TypeReference<Map<String, Boolean>>() {
        });
        translations = fromJson(readResourceFile("user-translations.json"), new TypeReference<Map<String, String>>() {
        });
    }

    public boolean isIgnorable(String text) {
        return ignorable.getOrDefault(text, false);
    }

    public String getTranslation(String text) {
        return translations.get(text);
    }

    public void setTranslation(String text, String translation) {
        // TODO: implement
        throw new NotImplementedException("Not implemented yet");
    }

    public void setIgnorable(String text, boolean ignorable) {
        // TODO: implement
        throw new NotImplementedException("Not implemented yet");
    }
}
