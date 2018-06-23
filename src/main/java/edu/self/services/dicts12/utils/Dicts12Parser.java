package edu.self.services.dicts12.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;

import edu.self.model.dicts12.DictsGroups;
import edu.self.model.dicts12.DictsWord;
import edu.self.utils.XMLUtils;

import static edu.self.utils.Config.CONFIG_ROOT;

public class Dicts12Parser {
    public static void main(String[] args) throws IOException, JAXBException {
        new Dicts12Parser().parse(CONFIG_ROOT + "\\12dicts\\2+2lemma.txt", CONFIG_ROOT + "\\12dicts_groups.xml");
    }

    public void parse(String inputFile, String outputFile) throws IOException, JAXBException {
        String file = FileUtils.readFileToString(new File(inputFile));
        // file = file.replaceAll("[a-zA-Z]|\\s|,|(->)|\\[|\\]", "");
        String[] lines = file.split("\n");
        DictsWord parent = null;
        Map<String, DictsWord> words = new HashMap<String, DictsWord>();
        for (String line : lines) {
            boolean isBase = !line.startsWith(" ");
            line = line.trim().replaceAll("\\[(.+?)\\]", "").replaceAll("->", "").replaceAll("\\+", "").trim();
            if (isBase) {
                DictsWord word = fromMap(words, line);
                parent = word;
            } else {
                for (String wordName : line.split(",")) {
                    wordName = wordName.trim();
                    if (!wordName.isEmpty()) {
                        DictsWord word = fromMap(words, wordName);
                        if (parent != null) {
                            parent.getChildren().add(word);
                            word.getParents().add(parent);
                        }
                    }
                }
            }
        }
        System.out.println("count=" + words.size());
        //Map<DictsWord, Integer> childCount = fetchChildCount(words);
        List<List<DictsWord>> groups = fetchGroups(words);
        DictsGroups dictsGroups = new DictsGroups();
        for (List<DictsWord> group : groups) {
            dictsGroups.getGroups().add(new DictsGroups.Group(group));
        }
        XMLUtils.save(outputFile, dictsGroups);
        System.out.println("count2=" + groups.size());
        // 80432
    }

    private List<List<DictsWord>> fetchGroups(Map<String, DictsWord> dictsWords) {
        List<List<DictsWord>> groups = new ArrayList<List<DictsWord>>();
        Set<DictsWord> words = new HashSet<DictsWord>(dictsWords.values());
        for (DictsWord word : dictsWords.values()) {
            if (words.contains(word)) {
                groups.add(fetchGroup(word, words));
            }
        }
        return groups;
    }

    private List<DictsWord> fetchGroup(DictsWord word, Set<DictsWord> words) {
        List<DictsWord> group = new ArrayList<DictsWord>();
        group.add(word);
        words.remove(word);
        for (DictsWord child : word.getChildren()) {
            if (words.contains(child)) {
                group.addAll(fetchGroup(child, words));
            }
        }
        for (DictsWord parent : word.getParents()) {
            if (words.contains(parent)) {
                group.addAll(fetchGroup(parent, words));
            }
        }
        return group;
    }

//	private Map<DictsWord, Integer> fetchChildCount(Map<String, DictsWord> dictsWords) {
//		Map<DictsWord, Integer> childCount = new HashMap<DictsWord, Integer>();
//		Collection<DictsWord> words = dictsWords.values();
//		int i = 0;
//		int max = 0;
//		for (DictsWord word : words) {
//			++i;
//			if (i % 100 == 0){
//				System.out.println(i);
//			}
//			int n = fetchChildCount(word, new HashSet<DictsWord>(words));
//			childCount.put(word, n);
//			if (n > max) {
//				max = n;
//				System.out.println("max=" + word.getText() + "=" + max);
//			}
//		}
//		return childCount;
//	}

//	private int fetchChildCount(DictsWord word, Set<DictsWord> words) {
//		words.remove(word);
//		int n = 0;
//		for (DictsWord child : word.getChildren()) {
//			if (words.contains(child)) {
//				n += 1 + fetchChildCount(child, words);
//			}
//		}
//		return n;
//	}

    private DictsWord fromMap(Map<String, DictsWord> words, String wordName) {
        DictsWord word = words.get(wordName);
        if (word == null) {
            word = new DictsWord(wordName);
            words.put(wordName, word);
        }
        return word;
    }

//	private boolean testLine(String line) {
//		line = rightTrim(line);
//		if (line.startsWith(" ") && !(line.startsWith("    ") && line.matches("^\\s{4}\\S.*"))) {
//			return false;
//		}
//		return true;
//	}

//	private String rightTrim(String string) {
//		int i = string.length() - 1;
//		while (i > -1 && Character.isWhitespace(string.charAt(i))) {
//			--i;
//		}
//		++i;
//		if (i == 0) {
//			return "";
//		}
//
//		if (i == string.length()) {
//			return string;
//		}
//		return string.substring(0, i);
//	}
}
