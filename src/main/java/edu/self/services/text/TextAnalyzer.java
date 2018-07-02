package edu.self.services.text;

import java.util.Map;

public interface TextAnalyzer {
    Map<String, Long> getWordOccurrences(String text);
}
