package com.example.netbooks.dao.interfaces;

import com.example.netbooks.models.Announcement;
import com.example.netbooks.models.Book;
import com.example.netbooks.models.ViewBook;

import java.util.Date;
import java.util.List;

public interface BookRepository {
    List<Book> findAllBooks();
    List<Book> findBooksByFilter(String title, String author, String genre, Date date1, Date date2, int page1, int page2);
    List<Announcement> findAllAnnouncement();
    String addBook(Book book);

    List<ViewBook> findAllViewBooks();
    List<ViewBook> findViewBooksByTitleOrAuthor(String titleOrAuthor);
    List<ViewBook> findViewBooksByFilter(String title, String author, String genre, Date date1, Date date2, int page1, int page2);
    ViewBook getBookById(int id);
}
