package com.OneOracle.LiterAlura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // Ignora cualquier campo desconocido en el JSON
public class Book {

    private int id;
    private String title;
    private List<Author> authors;
    private List<String> languages;
    private int downloadCount;
    private List<Author> translators;

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public List<Author> getTranslators() {
        return translators;
    }

    public void setTranslators(List<Author> translators) {
        this.translators = translators;
    }
}
