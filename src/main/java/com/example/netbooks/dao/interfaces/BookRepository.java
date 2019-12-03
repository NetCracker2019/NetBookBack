package com.example.netbooks.dao.interfaces;

import com.example.netbooks.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.util.List;

public interface BookRepository {
    List<Book> findAllBooks();
    List<Announcement> findAllAnnouncement();
    String addBook(Book book, int userId);
//    String addAnnouncement(Book book);
//    void addNewAnnouncement(Book book);
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
    List<ViewBook> findBooksByTitleAndGenre(String title, Integer genre, Date from, Date to);
    List<ViewAnnouncement> findViewUnApproveBooks();



    void confirmAnnouncement(long announcementId);
    void cancelAnnouncement(long announcementId);

    List<Event> getCalendarAllAnnouncement();
    List<Event> getCalendarPersonalizeAnnouncement(int userId);

    List<String> getFavouriteAuthor(int id);
    List<String> getFavouriteGenre(int id);

    void likeBook(long bookId);
    int countBooksForUser(long userId);
    void addBookBatchTo(Long userId, String shelf, List<Long> booksId);
    List<ViewBook> getBooksByUserId(Long id, String sought, int cntBooks, int offset, boolean read,
                                    boolean favourite, boolean reading, boolean notSet, String sortBy, String order);
    void removeBookBatch(long userId, List<Long> booksId);
    void removeBookBatchFrom(Long userId, String shelf, List<Long> booksId);
}
