package com.example.netbooks.dao.interfaces;

import com.example.netbooks.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    int getAmountOfBook();
    List<ViewBook> getPeaceBook(int page, int booksPerPage);

    List<ViewBook> getPeaceOfSearchBook(String titleOrAuthor, int page, int offset);
    List<ViewBook> getPeaceOfBook(int page, int offset);
    boolean addBookToProfile(long userId, long bookId);
    boolean checkBookInProfile(long userId, long bookId);
    boolean removeBookFromProfile(long userId, long bookId);


    List<ViewBook> findAllViewBooks();

    List<ViewBook> findViewBooksByTitleOrAuthor(String titleOrAuthor);
    ViewBook getBookById(int id);
    List<ViewBook> findBooksByTitleAndGenre(String title, String genre, Date from, Date to);
    List<ViewAnnouncement> findViewUnApproveBooks();

    public String addRowIntoBookAuthor(Book book);

    public String confirmAnnouncement(long announcementId);
    public String cancelAnnouncement(long announcementId);

    List<Event> getCalendarAllAnnouncement();
    List<Event> getCalendarPersonalizeAnnouncement();

    List<String> getFavouriteAuthor(int id);
    List<String> getFavouriteGenre(int id);
}
