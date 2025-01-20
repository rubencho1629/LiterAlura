package com.OneOracle.LiterAlura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GutendexResponse {

    private int count;
    private List<Book> results;

    // Getters y Setters
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Book> getResults() {
        return results;
    }

    public void setResults(List<Book> results) {
        this.results = results;
    }

    // toString()
    @Override
    public String toString() {
        return "GutendexResponse{" +
                "count=" + count +
                ", results=" + results +
                '}';
    }
}
