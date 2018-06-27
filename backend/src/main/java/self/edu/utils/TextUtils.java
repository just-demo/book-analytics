package self.edu.utils;

import java.util.HashSet;
import java.util.Set;

public class TextUtils {

    public static String trimNonLetters(String string) {
        //return string.replaceAll("^[^a-zA-Z]*(.*?)[^a-zA-Z]*$", "$1");
        int length = string.length();
        int start = 0;
        int end = length;
        while (start < end && !Character.isLetter(string.charAt(start))) {
            ++start;
        }

        while (start < end && !Character.isLetter(string.charAt(end - 1))) {
            --end;
        }
        return (start > 0 || end < length) ? string.substring(start, end) : string;
    }

    public static String cutEnding(String string) {
        return cutSuffix(string, "'s");
    }

    public static String cutSuffix(String string, String... suffixes) {
        for (String suffix : suffixes) {
            if (string.endsWith(suffix)) {
                return string.substring(0, string.length() - suffix.length());
            }
        }
        return string;
    }

    public static int upperCount(String string) {
        int n = 0;
        for (char c : string.toCharArray()) {
            if (Character.isUpperCase(c)) {
                ++n;
            }
        }
        return n;
    }

    public static Set<String> getAllCases(String... strings) {
        Set<String> cases = new HashSet<>();
        for (String string : strings) {
            cases.addAll(getAllCases(string));
        }
        return cases;
    }

    private static Set<String> getAllCases(String string) {
        Set<String> cases = new HashSet<>();
        if (string.length() == 0) {
            cases.add("");
        } else {
            char c = string.charAt(0);
            for (String ending : getAllCases(string.substring(1))) {
                cases.add(Character.toLowerCase(c) + ending);
                cases.add(Character.toUpperCase(c) + ending);
            }
        }
        return cases;
    }

    public static String capitalize(String string) {
        //return string.length() == 0 ? string : (Character.toUpperCase(string.charAt(0)) + string.substring(1).toLowerCase());
        if (string.isEmpty()) {
            return string;
        }
        char[] c = string.toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        for (int i = 1; i < c.length; ++i) {
            c[i] = Character.toLowerCase(c[i]);
        }
        return new String(c);
    }

    public static boolean isRoman(String string) {
        for (char c : string.toCharArray()) {
            if (c != 'I' && c != 'V' && c != 'X' && c != 'L' && c != 'C' && c != 'D' && c != 'M') {
                return false;
            }
        }
        return true;
    }
}
