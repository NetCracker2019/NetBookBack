package com.example.netbooks.dao;

import com.example.netbooks.models.Book;

import java.util.List;

public interface BookRepository {
    List<Book> findAll();
    List<Book> findBooksByTitle(String title);
    List<Book> findBooksByAuthor(String author);
    List<Book> findBooksByFilter(String title, String author, String genre, Date date1, Date date2, int page1, int page2);
    List<Announcement> findAllAnnouncement();
    String addBook(Book book);
    void save(Book book);
    //void update(Book book);
}