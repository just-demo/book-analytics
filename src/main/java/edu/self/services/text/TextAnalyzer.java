package edu.self.services.text;

import java.util.Map;

public interface TextAnalyzer {
    Map<String, Integer> getWordOccurrences(String text);
}
