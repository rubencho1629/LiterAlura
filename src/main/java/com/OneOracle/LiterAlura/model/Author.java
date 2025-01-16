package com.OneOracle.LiterAlura.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Author {

    private String name;

    @JsonProperty("birth_year") // Mapea el campo "birth_year" del JSON a "birthYear"
    private Integer birthYear;

    @JsonProperty("death_year") // Mapea el campo "death_year" del JSON a "deathYear"
    private Integer deathYear;

    // Getters y Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }
}
