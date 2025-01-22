package com.OneOracle.LiterAlura.controller;

import com.OneOracle.LiterAlura.model.Author;
import com.OneOracle.LiterAlura.model.Book;
import com.OneOracle.LiterAlura.service.GutendexService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class GutendexController {

    private final GutendexService gutendexService;

    public GutendexController(GutendexService gutendexService) {
        this.gutendexService = gutendexService;
    }

    // Endpoint para listar todos los libros
    @GetMapping("/all")
    public List<Book> getAllBooks() {
        return gutendexService.fetchBooks();
    }

    // Endpoint para listar todos los autores
    @GetMapping("/authors")
    public List<Author> getAllAuthors() {
        return gutendexService.listAllAuthors();
    }

    // Endpoint para listar autores vivos en un año específico
    @GetMapping("/authors/alive")
    public List<Author> getAuthorsAliveInYear(@RequestParam int year) {
        return gutendexService.listAuthorsAliveInYear(year);
    }

    // Endpoint para buscar un libro por título
    @GetMapping("/search")
    public Book searchBookByTitle(@RequestParam String title) {
        return gutendexService.fetchBookByTitle(title);
    }

    // Endpoint para listar todos los libros buscados
    @GetMapping("/searched")
    public List<Book> getAllSearchedBooks() {
        return gutendexService.listAllSearchedBooks();
    }

    // Endpoint para filtrar libros por idioma
    @GetMapping("/filter")
    public List<Book> filterBooksByLanguage(@RequestParam String language) {
        return gutendexService.filterBooksByLanguage(language);
    }

    // Endpoint para estadísticas de libros por idiomas
    @GetMapping("/statistics")
    public String getBookStatistics(@RequestParam List<String> languages) {
        return gutendexService.getBooksStatistics(languages);
    }
}
