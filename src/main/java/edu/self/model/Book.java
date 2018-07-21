package edu.self.model;

import java.util.Objects;

public class Book {
    private String name;
    private String content;
    private Language language;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(name, book.name) &&
                Objects.equals(content, book.content) &&
                language == book.language;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, content, language);
    }
}
