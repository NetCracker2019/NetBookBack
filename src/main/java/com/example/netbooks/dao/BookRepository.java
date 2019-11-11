package com.example.netbooks.dao;

import com.example.netbooks.models.Announcement;
import com.example.netbooks.models.Book;

import java.util.List;

public interface BookRepository {
    List<Book> findAll();
    List<Announcement> findAllAnnouncement();
    String addBook(Book book);
}