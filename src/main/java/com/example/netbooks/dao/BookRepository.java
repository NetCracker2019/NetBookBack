package com.example.netbooks.dao;

import com.example.netbooks.models.Book;

import java.util.List;

public interface BookRepository {
    List<Book> findAll();
}