package edu.self.services.google;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class GoogleUtils {

    public static String fetchTranslation(String googleResponseJson) {
        String translation = "";
        Gson gson = new Gson();
        Object googleResponce = gson.fromJson(googleResponseJson, Object.class);
        Object googleTranslation = getInnerElement(googleResponce, 3);
        if (googleTranslation instanceof String) {
            translation = (String) googleTranslation;
        }
        return translation;
    }

    private static Object getInnerElement(Object object, int depth) {
        if (depth == 0) {
            return object;
        }
        if (object instanceof List) {
            List list = (List) object;
            if (!list.isEmpty()) {
                return getInnerElement(list.get(0), depth - 1);
            }
        }
        return null;
    }

    private static <T> T getChildElement(Object object, int index, Class<T> clazz) {
        if (object instanceof List) {
            List list = (List) object;
            if (list.size() > index) {
                Object result = list.get(index);
                if (clazz.isInstance(result)) {
                    return (T) result;
                }
            }
        }
        return null;
    }

    public static List<String> fetchTranslations(String googleResponseJson) {
        List<String> translations = new ArrayList<String>();
        Gson gson = new Gson();
        Object googleResponce = gson.fromJson(googleResponseJson, Object.class);
        List list = getChildElement(googleResponce, 1, List.class);
        if (list != null) {
            for (Object group : list) {
                String groupName = getChildElement(group, 0, String.class);
                List words = getChildElement(group, 1, List.class);
                if (groupName != null && !groupName.isEmpty() && words != null) {
                    for (Object word : words) {
                        if (word instanceof String) {
                            translations.add((String) word);
                        }
                    }
                }
            }
        }
        return translations;
    }
}
