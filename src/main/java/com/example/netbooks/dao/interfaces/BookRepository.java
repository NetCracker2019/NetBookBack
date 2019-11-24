package com.example.netbooks.dao.interfaces;

import com.example.netbooks.models.Announcement;
import com.example.netbooks.models.Book;
import com.example.netbooks.models.ViewAnnouncement;
import com.example.netbooks.models.ViewBook;

import java.util.Date;
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
    List<ViewAnnouncement> findViewUnApproveBooks();
    List<ViewBook> findViewBooksByTitleOrAuthor(String titleOrAuthor);
    ViewBook getBookById(int id);

    public String addRowIntoBookAuthor(Book book);

    public String confirmAnnouncement(long announcementId);
    public String cancelAnnouncement(long announcementId);

    List<ViewBook> findBooksByTitleAndGenre(String title, String genre, java.sql.Date from, java.sql.Date to);

}
