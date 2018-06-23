package edu.self.services.google.translate;

import java.util.List;

public interface GoogleTranslate {
    String translate(String text);

    List<String> getTranslations(String text);
}
