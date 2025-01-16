package com.OneOracle.LiterAlura.service;

import com.OneOracle.LiterAlura.model.Book;
import com.OneOracle.LiterAlura.model.GutendexResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class GutendexService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public GutendexService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<Book> fetchBooks() {
        String apiUrl = "https://gutendex.com/books/";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                // Deserializar el objeto completo
                GutendexResponse gutendexResponse = objectMapper.readValue(response.body(), GutendexResponse.class);
                // Devolver solo la lista de resultados
                return gutendexResponse.getResults();
            } else {
                throw new RuntimeException("Failed to fetch books. HTTP status: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error during HTTP request: " + e.getMessage(), e);
        }
    }
}
