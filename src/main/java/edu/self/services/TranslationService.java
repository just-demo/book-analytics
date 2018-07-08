package edu.self.services;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@Service
public class TranslationService {
    @Autowired
    private Map<String, List<String>> translations;

    public String translate(String text) {
        return ofNullable(text)
                .map(String::trim)
                .map(translations::get)
                .filter(CollectionUtils::isNotEmpty)
                .map(Collection::iterator)
                .map(Iterator::next)
                .orElse("");
    }

    public List<String> getTranslations(String text) {
        return ofNullable(text)
                .map(String::trim)
                .map(translations::get)
                .orElse(emptyList());
    }

    public Map<String, List<String>> getTranslations() {
        return translations;
    }
}
