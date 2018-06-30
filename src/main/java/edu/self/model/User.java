package edu.self.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Document(collection = "users")
public class User {
    @Id
    private String username;
    private Map<String, String> translations = new HashMap<>();
    private Set<String> ignored = new HashSet<>();
    private Set<String> selected = new HashSet<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<String, String> getTranslations() {
        return translations;
    }

    public void setTranslations(Map<String, String> translations) {
        this.translations = translations;
    }

    public Set<String> getIgnored() {
        return ignored;
    }

    public void setIgnored(Set<String> ignored) {
        this.ignored = ignored;
    }

    public Set<String> getSelected() {
        return selected;
    }

    public void setSelected(Set<String> selected) {
        this.selected = selected;
    }
}
