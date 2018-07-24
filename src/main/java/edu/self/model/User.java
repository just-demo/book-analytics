package edu.self.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Document(collection = "users")
public class User {
    @Id
    private String username;
    private Set<Map<Language, String>> selected = new HashSet<>();
    private Set<String> hidden = new HashSet<>();
    private Set<UserBook> books = new HashSet<>();

    public User() {
        // for json deserialization
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Map<Language, String>> getSelected() {
        return selected;
    }

    public void setSelected(Set<Map<Language, String>> selected) {
        this.selected = selected;
    }

    public Set<String> getHidden() {
        return hidden;
    }

    public void setHidden(Set<String> hidden) {
        this.hidden = hidden;
    }

    public Set<UserBook> getBooks() {
        return books;
    }

    public void setBooks(Set<UserBook> books) {
        this.books = books;
    }
}
