package com.OneOracle.LiterAlura.controller;

import com.OneOracle.LiterAlura.model.Book;
import com.OneOracle.LiterAlura.service.GutendexService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GutendexController {

    private final GutendexService gutendexService;

    public GutendexController() {
        this.gutendexService = new GutendexService();
    }

    @GetMapping("/books")
    public List<Book> getBooks() {
        return gutendexService.fetchBooks();
    }
}
