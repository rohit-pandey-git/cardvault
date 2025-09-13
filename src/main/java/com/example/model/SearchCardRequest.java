package com.example.model;

import jakarta.validation.constraints.NotBlank;

public class SearchCardRequest {
    @NotBlank
    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}