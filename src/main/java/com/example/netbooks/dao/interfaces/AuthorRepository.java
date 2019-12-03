package com.example.netbooks.dao.interfaces;

import com.example.netbooks.models.Author;
import com.example.netbooks.models.Book;

import java.util.List;

public interface AuthorRepository {
    List<Author> getAllAuthors();

    String addRowIntoBookAuthor(String title, List<String> id);

}
