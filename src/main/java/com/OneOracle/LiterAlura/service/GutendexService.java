package com.OneOracle.LiterAlura.service;


import com.OneOracle.LiterAlura.model.Author;
import com.OneOracle.LiterAlura.model.Book;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GutendexService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final List<Book> searchedBooks;

    public GutendexService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.searchedBooks = new ArrayList<>();
    }

    // Método existente: Obtener libros desde la API
    public List<Book> fetchBooks() {
        String apiUrl = "https://gutendex.com/books/";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                JsonNode results = root.get("results");

                List<Book> books = new ArrayList<>();
                for (JsonNode node : results) {
                    Book book = parseBookFromJson(node);
                    books.add(book);
                }
                return books;
            } else {
                throw new RuntimeException("Failed to fetch books. HTTP status: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error during HTTP request: " + e.getMessage(), e);
        }
    }
    // Nueva funcionalidad: Obtener lista de autores
    public List<Author> listAllAuthors() {
        return searchedBooks.stream()
                .map(book -> book.getAuthors().get(0)) // Considerar solo el primer autor
                .distinct() // Evitar autores duplicados
                .collect(Collectors.toList());
    }

    // Nueva funcionalidad: Filtrar autores vivos en un año determinado
    public List<Author> listAuthorsAliveInYear(int year) {
        return listAllAuthors().stream()
                .filter(author -> (author.getBirthYear() <= year) &&
                        (author.getDeathYear() == null || author.getDeathYear() >= year))
                .collect(Collectors.toList());
    }

    // Método para agregar libros a la lista buscada
    public void addBookToSearchedList(Book book) {
        searchedBooks.add(book);
    }
    // Nueva funcionalidad: Buscar un libro por título
    public Book fetchBookByTitle(String title) {
        String apiUrl = "https://gutendex.com/books/?search=" + title.replace(" ", "%20");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                JsonNode firstResult = root.get("results").get(0);

                Book book = parseBookFromJson(firstResult);

                // Guardar el libro en la lista local
                searchedBooks.add(book);
                return book;
            } else {
                throw new RuntimeException("Error fetching book by title. HTTP status: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error fetching book by title: " + e.getMessage(), e);
        }
    }

    // Nueva funcionalidad: Listar todos los libros buscados
    public List<Book> listAllSearchedBooks() {
        return new ArrayList<>(searchedBooks);
    }
    public List<Book> filterBooksByAuthor(String authorName) {
        return listAllSearchedBooks().stream()
                .filter(book -> book.getAuthors().stream()
                        .anyMatch(author -> author.getName().equalsIgnoreCase(authorName)))
                .collect(Collectors.toList());
    }

    // Nueva funcionalidad: Filtrar libros por idioma
    public List<Book> filterBooksByLanguage(String language) {
        return searchedBooks.stream()
                .filter(book -> book.getLanguages().get(0).equalsIgnoreCase(language))
                .collect(Collectors.toList());
    }
    public List<Book> sortBooksByDownloads() {
        return listAllSearchedBooks().stream()
                .sorted((b1, b2) -> Integer.compare(b2.getDownloadCount(), b1.getDownloadCount()))
                .collect(Collectors.toList());
    }

    // Método privado para convertir JSON en objeto Book
    private Book parseBookFromJson(JsonNode node) {
        Book book = new Book();
        book.setId(node.get("id").asInt());
        book.setTitle(node.get("title").asText());

        JsonNode authorNode = node.get("authors").get(0);
        Author author = new Author();
        author.setName(authorNode.get("name").asText());
        author.setBirthYear(authorNode.has("birth_year") ? authorNode.get("birth_year").asInt() : null);
        author.setDeathYear(authorNode.has("death_year") ? authorNode.get("death_year").asInt() : null);

        book.setAuthors(List.of(author));
        book.setLanguages(List.of(node.get("languages").get(0).asText()));
        book.setDownloadCount(node.get("download_count").asInt());
        return book;
    }
}
