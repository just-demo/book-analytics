package edu.self.dto;

import java.util.List;

public class Statistics {
    private String text;
    private String translation;
    private List<String> translations;
    private long occurrence;
    private boolean ignored;
    private boolean managed;

    public Statistics(String text, String translation, long occurrence, boolean ignored, boolean managed, List<String> translations) {
        this.text = text;
        this.translation = translation;
        this.occurrence = occurrence;
        this.ignored = ignored;
        this.managed = managed;
        this.translations = translations;
    }

    public String getText() {
        return text;
    }

    public String getTranslation() {
        return translation;
    }

    public List<String> getTranslations() {
        return translations;
    }

    public long getOccurrence() {
        return occurrence;
    }

    public boolean isIgnored() {
        return ignored;
    }

    public boolean isManaged() {
        return managed;
    }
}
