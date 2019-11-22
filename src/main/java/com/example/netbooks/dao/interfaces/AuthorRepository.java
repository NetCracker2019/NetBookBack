package com.example.netbooks.dao.interfaces;

import com.example.netbooks.models.Author;

import java.util.List;

public interface AuthorRepository {
    List<Author> getAllAuthors();
}
