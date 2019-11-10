package com.example.netbooks.controllers;

import com.example.netbooks.dao.JdbcBookRepository;
import com.example.netbooks.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {
    @Autowired
    private JdbcBookRepository jdbcBookRepository;

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return jdbcBookRepository.findAll();
    }
}