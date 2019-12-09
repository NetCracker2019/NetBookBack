package com.example.netbooks.dao.interfaces;

import com.example.netbooks.models.*;

import java.sql.Date;
import java.util.List;

public interface BookRepository {
    List<Book> findAllBooks();
    List<Announcement> findAllAnnouncement();
    String addBook(Book book, int userId);
//    String addAnnouncement(Book book);
//    void addNewAnnouncement(Book book);
    int getAmountOfAnnouncement();
    List<ViewBook> getPeaceAnnouncement(int page, int booksPerPage);

    int getAmountOfBook();
    List<ViewBook> getPeaceBook(int page, int booksPerPage);

    List<ViewBook> getPeaceOfSearchBook(String titleOrAuthor, int page, int offset);
    List<ViewBook> getPeaceOfBook(int page, int offset);
    boolean addBookToProfile(long userId, long bookId);
    boolean checkBookInProfile(long userId, long bookId);
    boolean removeBookFromProfile(long userId, long bookId);


    List<ViewBook> findAllViewBooks();

    List<ViewBook> findViewBooksByTitle(String title);
    ViewBook getBookById(int id);
    List<ViewBook> findBooksByTitleAndGenre(String title, Integer genre, Date from, Date to);
    List<ViewBook> findBooksByTitleAndAuthor(String title, String author, Date from, Date to);
    List<ViewBook> findBooksByTitleAndDate(String title, Date from, Date to);
    List<ViewBook> findBooksByTitleAndAuthorAndGenre(String title, String author, Integer genre, Date from, Date to);
    List<ViewAnnouncement> findViewUnApproveBooks();



    void confirmAnnouncement(long announcementId);
    void cancelAnnouncement(long announcementId);

    List<Event> getCalendarAllAnnouncement();
    List<Event> getCalendarPersonalizeAnnouncement(int userId);

    List<String> getFavouriteAuthor(int id);
    List<String> getFavouriteGenre(int id);

    //void likeBook(long bookId);
    int countBooksForUser(long userId);
    //void addBookBatchTo(Long userId, String shelf, List<Long> booksId);
    List<ViewBook> getBooksByUserId(Long id, String sought, int cntBooks, int offset, boolean read,
                                    boolean favourite, boolean reading, boolean notSet, String sortBy, String order);
    void removeBookBatch(long userId, List<Long> booksId);
    void removeBookBatchFrom(Long userId, String shelf, List<Long> booksId);

    List<ViewBook> getSuggestions(long userId);
  
    void likeBook(long bookId, long userId);
    void dislikeBook(long bookId, long userId);
    int checkLickedBook(long bookId, long userId);

    void addBookBatchToReading(Long userId,  List<Long> booksId);
    void addBookBatchToFavourite(Long userId,  List<Long> booksId);
    void addBookBatchToRead(Long userId,  List<Long> booksId);
    int countAddedBooksForUser(long userId);
}
