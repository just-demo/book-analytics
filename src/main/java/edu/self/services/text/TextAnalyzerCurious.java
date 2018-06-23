package edu.self.services.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.self.model.WordInfo;
import edu.self.model.user.UserWord;
import edu.self.services.google.translate.GoogleTranslate;
import edu.self.utils.TextUtils;

public class TextAnalyzerCurious implements TextAnalyzer {
    private GoogleTranslate google;

    public TextAnalyzerCurious(GoogleTranslate google) {
        this.google = google;
    }

    @Override
    public Map<String, Integer> getWordOccurrencies(String text) {
        return null; //not implemented
    }

    @Override
    public Map<String, WordInfo> getWordStatistics(String text) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<UserWord> getWords(String text) {
        Map<String, UserWord> words = new HashMap<String, UserWord>();
        String[] wordArray = text.split("\\s+");
        for (String wordText : wordArray) {
            wordText = TextUtils.cutEnding(TextUtils.trimNonLetters(wordText));
            if (!wordText.isEmpty()) {
                String wordLowerCase = wordText.toLowerCase();
                if (words.containsKey(wordLowerCase)) {
                    words.get(wordLowerCase).incrementCount();
                } else {
                    String translation = google.translate(wordLowerCase);
                    UserWord word = new UserWord(wordText, translation);
                    if (!translation.isEmpty()) {
                        if (Character.isUpperCase(translation.charAt(0))) {
                            word.setProper();
                        } else if (!translation.equals(wordLowerCase) && translation.toLowerCase().equals(translation)) {
                            word.setCommon();
                            word.setText(wordLowerCase);
                        }
                    }
                    words.put(wordLowerCase, word);
                }
            }
        }
        return new ArrayList(words.values());
    }

//	private void logStatistics(Map<String, Word> words){
//		int common = 0;
//		int proper = 0;
//				
//		for (String wordText: words.keySet()){
//			Word word = words.get(wordText);
//			if (word.isCommon()){
//				++common;
//			}
//			else if (word.isProper()){
//				++proper;
//			}
//		}
//		int undefined = words.keySet().size() - (proper + common);
//		Logger.getAnonymousLogger().log(Level.INFO, "statistics: " + common + "/" + proper + "/" + undefined);
//	}
}
