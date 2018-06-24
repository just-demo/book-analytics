package edu.self.services.text;

import java.util.List;
import java.util.Map;

import edu.self.model.WordInfo;
import edu.self.model.UserWord;

public interface TextAnalyzer {
    Map<String, Integer> getWordOccurrences(String text);

    Map<String, WordInfo> getWordStatistics(String text);

    List<UserWord> getWords(String text);
}
