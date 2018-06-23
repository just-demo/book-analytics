package edu.self.services.user;

import java.util.HashMap;
import java.util.Map;

import edu.self.model.user.UserWord;
import edu.self.model.user.UserWords;
import edu.self.utils.XMLUtils;

import static edu.self.utils.Config.CONFIG_ROOT;

public class UserWordsService {
    private UserWords words;
    private Map<String, UserWord> wordsMap;
    private String file;

    public UserWordsService(String file) {
        this.file = file;
        load();
    }

    public UserWordsService() {
        this(CONFIG_ROOT + "\\user\\words.xml"); //TODO: make is configurable!!!
    }

    private void save() {
        try {
            XMLUtils.save(file, words);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void load() {
        try {
            words = XMLUtils.load(file, UserWords.class);
            wordsMap = new HashMap<String, UserWord>();
            for (UserWord word : words.getWords()) {
                wordsMap.put(word.getText(), word);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //@Override
    @Deprecated
    public Map<String, UserWord> getWords() {
        return wordsMap;
    }

    public boolean isIgnorable(String text) {
        UserWord word = wordsMap.get(text);
        return word != null && word.isIgnorable();
    }

    public String getTranslation(String text) {
        UserWord word = wordsMap.get(text);
        return word != null ? word.getTranslation() : null;
    }

    //@Override
    public void setTranslation(String text, String translation) {
        UserWord word = getStorableWord(text.trim());
        word.setTranslation(translation.trim());
        save();
    }

    //@Override
    public void setIgnorable(String text, boolean ignorable) {
        UserWord word = getStorableWord(text.trim());
        word.setIgnorable(ignorable);
        save();
    }

    private UserWord getStorableWord(String text) {
        UserWord word = wordsMap.get(text);
        if (word == null) {
            word = new UserWord();
            word.setText(text);
            words.getWords().add(word);
            wordsMap.put(text, word);
        }
        return word;
    }

}
