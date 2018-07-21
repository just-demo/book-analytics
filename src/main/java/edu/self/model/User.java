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
    private Map<String, Set<String>> selected = new HashMap<>();
    private Set<String> hidden = new HashSet<>();
    // TODO: implement book bodies as references to another collection
    private Map<String, String> books = new HashMap<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<String, Set<String>> getSelected() {
        return selected;
    }

    public void setSelected(Map<String, Set<String>> selected) {
        this.selected = selected;
    }

    public Set<String> getHidden() {
        return hidden;
    }

    public void setHidden(Set<String> hidden) {
        this.hidden = hidden;
    }

    public Map<String, String> getBooks() {
        return books;
    }

    public void setBooks(Map<String, String> books) {
        this.books = books;
    }
}
