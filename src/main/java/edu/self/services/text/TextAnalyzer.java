package edu.self.services.text;

import java.util.List;
import java.util.Map;

import edu.self.model.WordInfo;
import edu.self.model.user.UserWord;

public interface TextAnalyzer {
    Map<String, Integer> getWordOccurrencies(String text);

    Map<String, WordInfo> getWordStatistics(String text);

    List<UserWord> getWords(String text);
}
