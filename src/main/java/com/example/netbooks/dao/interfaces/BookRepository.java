package com.example.netbooks.dao.interfaces;

import com.example.netbooks.models.Announcement;
import com.example.netbooks.models.Book;
import com.example.netbooks.models.ViewBook;

import java.sql.Date;
import java.util.List;

public interface BookRepository {
    List<Book> findAllBooks();
    List<Announcement> findAllAnnouncement();
    String addBook(Book book);
    String addAnnouncement(Book book);
    void addNewAnnouncement(Book book);
    int getAmountOfAnnouncement();
    List<Announcement> getPeaceAnnouncement(int page, int booksPerPage);
    List<ViewBook> getPeaceOfSearchBook(String titleOrAuthor, int page, int offset);
    List<ViewBook> getPeaceOfBook(int page, int offset);


    List<ViewBook> findAllViewBooks();
    List<ViewBook> findViewBooksByTitleOrAuthor(String titleOrAuthor, int size, int startIndex);
    ViewBook getBookById(int id);
    List<ViewBook> findBooksByTitleAndGenre(String title, String genre, Date from, Date to, int size, int startIndex);
}
