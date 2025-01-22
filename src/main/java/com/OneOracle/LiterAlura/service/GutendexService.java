package com.OneOracle.LiterAlura.service;

import com.OneOracle.LiterAlura.model.Author;
import com.OneOracle.LiterAlura.model.Book;
import com.OneOracle.LiterAlura.repository.AuthorRepository;
import com.OneOracle.LiterAlura.repository.BookRepository;
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
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public GutendexService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    // Método para obtener libros desde la API y guardarlos en la base de datos
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
                    saveBook(book); // Guardar en la base de datos
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

    // Guardar un libro y su autor en la base de datos
    public void saveBook(Book book) {
        // Verificar si el autor ya existe en la base de datos
        Author author = book.getAuthors().get(0);
        if (author != null) {
            Author existingAuthor = authorRepository.findByName(author.getName())
                    .orElseGet(() -> authorRepository.save(author));
            book.setAuthors(List.of(existingAuthor));
        }

        bookRepository.save(book);
    }

    // Lista de todos los autores en la base de datos
    public List<Author> listAllAuthors() {
        return authorRepository.findAll();
    }

    // Lista de autores vivos en un año determinado
    public List<Author> listAuthorsAliveInYear(int year) {
        return listAllAuthors().stream()
                .filter(author -> (author.getBirthYear() <= year) &&
                        (author.getDeathYear() == null || author.getDeathYear() >= year))
                .collect(Collectors.toList());
    }

    // Buscar un libro por título desde la API y guardarlo
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
                saveBook(book); // Guardar en la base de datos
                return book;
            } else {
                throw new RuntimeException("Error fetching book by title. HTTP status: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error fetching book by title: " + e.getMessage(), e);
        }
    }

    // Listar todos los libros buscados
    public List<Book> listAllSearchedBooks() {
        return bookRepository.findAll();
    }

    // Filtrar libros por autor
    public List<Book> filterBooksByAuthor(String authorName) {
        return bookRepository.findAll().stream()
                .filter(book -> book.getAuthors().stream()
                        .anyMatch(author -> author.getName().equalsIgnoreCase(authorName)))
                .collect(Collectors.toList());
    }

    // Filtrar libros por idioma
    public List<Book> filterBooksByLanguage(String language) {
        return bookRepository.findAll().stream()
                .filter(book -> book.getLanguages().get(0).equalsIgnoreCase(language))
                .collect(Collectors.toList());
    }

    // Ordenar libros por número de descargas
    public List<Book> sortBooksByDownloads() {
        return bookRepository.findAll().stream()
                .sorted((b1, b2) -> Integer.compare(b2.getDownloadCount(), b1.getDownloadCount()))
                .collect(Collectors.toList());
    }

    // Obtener estadísticas de libros por idiomas
    public String getBooksStatistics(List<String> languages) {
        StringBuilder stats = new StringBuilder();
        stats.append("\n==== Estadísticas de Libros ====\n");
        for (String language : languages) {
            long count = bookRepository.countByLanguagesContaining(language);
            stats.append("Idioma: ").append(language).append(", Cantidad de Libros: ").append(count).append("\n");
        }
        return stats.toString();
    }

    // Método privado para convertir JSON en objeto Book
    private Book parseBookFromJson(JsonNode node) {
        Book book = new Book();
        book.setId(Long.valueOf(node.get("id").asInt())); // Conversión de int a Long
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
