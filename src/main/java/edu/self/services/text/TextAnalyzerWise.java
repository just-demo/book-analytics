package edu.self.services.text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.self.model.WordInfo;
import edu.self.model.UserWord;
import edu.self.utils.TextUtils;

public class TextAnalyzerWise implements TextAnalyzer {
    @Override
    public Map<String, Integer> getWordOccurrences(String text) {
        List<WiseWord> words = getWordsPrepared(text);
        return getWordOccurrencies(words);
    }

    @Override
    public Map<String, WordInfo> getWordStatistics(String text) {
        List<WiseWord> words = getWordsPrepared(text);
        return getWordStatistics(words);
    }

    private Map<String, WordInfo> getWordStatistics(List<WiseWord> words) {
        Map<String, WordInfo> wordStatistics = new TreeMap<>();
        for (WiseWord word : words) {

            String wordString = word.getResult();
            WordInfo wordInfo;
            if (wordStatistics.containsKey(wordString)) {
                wordInfo = wordStatistics.get(wordString);
            } else {
                wordInfo = new WordInfo();
                wordStatistics.put(wordString, wordInfo);
            }
            wordInfo.incrementTotal();
            if (word.isCommon()) {
                wordInfo.incrementCommon();
            }
            if (word.isProper()) {
                wordInfo.incrementProper();
            }
            if (word.isStrange()) {
                wordInfo.incrementStrange();
            }
            if (word.isUndefined()) {
                wordInfo.incrementUndefined();
            }
        }
        return wordStatistics;
    }

    @Override
    public List<UserWord> getWords(String text) {
        // TODO Auto-generated method stub
        return null;
    }

    private List<WiseWord> getWordsPrepared(String text) {
        WiseWord previous = null;
        Set<String> common = new HashSet<>();
        Set<String> proper = new HashSet<>();
        List<WiseWord> words = new ArrayList<>();
        String[] wordArray = text.split("\\s+");
        for (String wordString : wordArray) {
            if (!wordString.isEmpty()) {
                WiseWord word = new WiseWord(wordString);
                word.clarify(previous);
                if (word.isCommon()) {
                    common.add(word.getResult());
                } else if (word.isProper()) {
                    proper.add(word.getResult());
                }
                words.add(word);
                previous = word;
            }
        }

        for (WiseWord word : words) {
            word.clarify(common, proper);
        }
        return words;
    }

    private Map<String, Integer> getWordOccurrencies(List<WiseWord> words) {
        Map<String, Integer> wordOccurrencies = new TreeMap<>();
        for (WiseWord word : words) {
            String wordString = word.getResult();
            wordOccurrencies.put(wordString, wordOccurrencies.containsKey(wordString) ? wordOccurrencies.get(wordString) + 1 : 1);
        }
        return wordOccurrencies;
    }

    private static class WiseWord {
        private enum Type {
            UNDEFINED, COMMON, PROPER, STRANGE
        }

        private String original;
        private String result;
        private String lowercase;
        private Type type = Type.UNDEFINED;

        WiseWord(String word) {
            original = word;
            result = TextUtils.cutEnding(TextUtils.trimNonLetters(word));
            lowercase = result.toLowerCase();
            if ("I".equals(result) || lowercase.equals(result)) {
                type = Type.COMMON;
            } else if (Character.isLowerCase(result.charAt(0))) {
                type = Type.STRANGE;
            }
        }

        public void clarify(WiseWord previous) {
            if (type.equals(Type.UNDEFINED) && previous != null && Character.isLetter(previous.original.charAt(previous.original.length() - 1))) {
                type = Type.PROPER;
            }
        }

        public void clarify(Set<String> common, Set<String> proper) {
            if (type.equals(Type.UNDEFINED)) {
                if (common.contains(lowercase)) {
                    if (!proper.contains(result)) {
                        type = Type.COMMON;
                        result = lowercase;
                    }
                } else if (proper.contains(result)) {
                    type = Type.PROPER;
                }
            }
        }

        public String getResult() {
            return result;
        }

        public boolean isCommon() {
            return type.equals(Type.COMMON);
        }

        public boolean isProper() {
            return type.equals(Type.PROPER);
        }

        public boolean isUndefined() {
            return type.equals(Type.UNDEFINED);
        }

        public boolean isStrange() {
            return type.equals(Type.STRANGE);
        }
    }
}
