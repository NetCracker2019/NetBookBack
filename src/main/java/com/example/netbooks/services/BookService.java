package com.example.netbooks.services;

import com.example.netbooks.dao.implementations.ReviewRepositoryImpl;
import com.example.netbooks.dao.implementations.UserRepository;
import com.example.netbooks.dao.interfaces.AuthorRepository;
import com.example.netbooks.dao.interfaces.GenreRepository;
import com.example.netbooks.dao.implementations.JdbcBookRepository;
import com.example.netbooks.dao.interfaces.ReviewRepository;
import com.example.netbooks.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    final JdbcBookRepository jdbcBookRepository;
    final GenreRepository genreRepository;
    final AuthorRepository authorRepository;
    final ReviewRepository reviewRepository;
    final UserRepository userRepository;

    public BookService(JdbcBookRepository jdbcBookRepository, GenreRepository genreRepository, AuthorRepository authorRepository, ReviewRepository reviewRepository, UserRepository userRepository) {
        this.jdbcBookRepository = jdbcBookRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    public List<ViewBook> findBooks(String searchString, int size, int page) {
        String processedString = searchString.toLowerCase().trim().replaceAll(" +", " ");
        int startIndex = size * (page - 1);
        return jdbcBookRepository.findViewBooksByTitleOrAuthor(processedString, size, startIndex);
    }

    public List<Book> getAllBooks() {
        return jdbcBookRepository.findAllBooks();
    }

    public List<ViewBook> getAllViewBooks() {
        return jdbcBookRepository.findAllViewBooks();
    }

    public List<ViewAnnouncement> getViewUnApproveBooks() {
        return jdbcBookRepository.findViewUnApproveBooks();
    }

    public int countReviews(boolean approved) {
        return reviewRepository.countReviews(approved);
    }

    public int countBooks() {
        return jdbcBookRepository.countBooks();
    }

    //    public List<Book> filterBooks(String title, String author, String genre, String strDate1, String strDate2, int page1, int page2){
//        String processedTitle = title.toLowerCase().trim().replaceAll(" +", " ");
//        String processedAuthor = author.toLowerCase().trim().replaceAll(" +", " ");
//        Date date1 = null;
//        Date date2 = null;
//        try {
//            date1 = new SimpleDateFormat("yyyy-mm-dd").parse(strDate1);
//            date2 = new SimpleDateFormat("yyyy-mm-dd").parse(strDate2);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        if (page1 > 0 && page2>page1){
//            return jdbcBookRepository.findBooksByFilter(processedTitle, processedAuthor, genre, date1, date2, page1, page2);
//        }
//        return null;
//    }
    public List<Review> getReviewsForBook(int id) {
        return reviewRepository.getReviewsByBookId(id);
    }

    public ViewBook getViewBookById(int id) {
        return jdbcBookRepository.getBookById(id);
    }

    public String addBook(Book book, String value) {
        if (!jdbcBookRepository.checkIsExist(book)) {
            jdbcBookRepository.addBook(book);
            genreRepository.addRowIntoBookGenre(book);
            jdbcBookRepository.addRowIntoBookAuthor(book);
        }
        if (value.equals("announcement")) {
            jdbcBookRepository.addNewAnnouncement(book);
        }
        return "Ok";
    }

    public String confirmAnnouncement(long announcementId) {
        jdbcBookRepository.confirmAnnouncement(announcementId);
        return "ok";
    }

    public String cancelAnnouncement(long announcementId) {
        jdbcBookRepository.cancelAnnouncement(announcementId);
        return "ok";
    }

    public List<Event> calendarAnnouncement(String value) {
        if (value.equals("all")) {
            return jdbcBookRepository.getCalendarAllAnnouncement();
        } else {
            return jdbcBookRepository.getCalendarPersonalizeAnnouncement();
        }
    }

    public String addAnnouncement(Book book) {
        return jdbcBookRepository.addAnnouncement(book);
    }

    public List<Announcement> findAllAnnouncement() {
        return jdbcBookRepository.findAllAnnouncement();
    }

    public int getAmountOfAnnouncement() {
        return jdbcBookRepository.getAmountOfAnnouncement();
    }


    public int getAmountOfBook() {
        return jdbcBookRepository.getAmountOfBook();
    }

    public List<ViewBook> getPeaceBook(int page, int booksPerPage) {
        return jdbcBookRepository.getPeaceBook(page, booksPerPage);
    }


    public List<Announcement> getPeaceAnnouncement(int page, int booksPerPage) {
        return jdbcBookRepository.getPeaceAnnouncement(page, booksPerPage);
    }


    public List<Genre> getAllGenres() {
        return genreRepository.getAllGenres();
    }

    public List<Author> getAllAuthors() {
        return authorRepository.getAllAuthors();
    }

    public List<ViewBook> getPeaceOfSearchBook(String searchString, int count, int offset) {
        return jdbcBookRepository.getPeaceOfSearchBook(searchString, count, offset);
    }

    public List<Review> getPeaceOfReviewByBook(int bookId, int count, int offset) {
        return reviewRepository.getPeaceOfReviewByBook(bookId, count, offset);
    }

    public List<ViewBook> getPeaceOfBooks(int count, int offset) {
        return jdbcBookRepository.getPeaceOfBook(count, offset);
    }

    public List<ViewBook> getBooksByTitleAndGenre(String title, String genre, Date from, Date to, int size, int page) {
        int startIndex = size * (page - 1);
        String processedTitle = title.toLowerCase().trim().replaceAll(" +", " ");
        return jdbcBookRepository.findBooksByTitleAndGenre(processedTitle, genre, from, to, size, startIndex);
    }

    public int getAmountBooksByTitleAndGenre(String title, String genre, Date from, Date to) {
        String processedTitle = title.toLowerCase().trim().replaceAll(" +", " ");
        return jdbcBookRepository.getAmountBooksByTitleAndGenre(processedTitle, genre, from, to);
    }

    public List<ViewBook> getBooksByTitleAndAuthor(String title, String author, Date from, Date to, int size, int page) {
        int startIndex = size * (page - 1);
        String processedTitle = title.toLowerCase().trim().replaceAll(" +", " ");
        return jdbcBookRepository.findBooksByTitleAndAuthor(processedTitle, author, from, to, size, startIndex);
    }

    public int getAmountBooksByTitleAndAuthor(String title, String author, Date from, Date to) {
        String processedTitle = title.toLowerCase().trim().replaceAll(" +", " ");
        return jdbcBookRepository.getAmountBooksByTitleAndAuthor(processedTitle, author, from, to);
    }

    public List<ViewBook> getBooksByTitleAndDate(String title, Date from, Date to, int size, int page) {
        String processedTitle = title.toLowerCase().trim().replaceAll(" +", " ");
        int startIndex = size * (page - 1);
        return jdbcBookRepository.findBooksByTitleAndDate(processedTitle, from, to, size, startIndex);
    }

    public int getAmountBooksByTitleAndDate(String title, Date from, Date to) {
        String processedTitle = title.toLowerCase().trim().replaceAll(" +", " ");
        return jdbcBookRepository.getAmountBooksByTitleAndDate(processedTitle, from, to);
    }

    public List<ViewBook> getBooksByTitleAndAuthorAndGenre(String title, String author, String genre, Date from, Date to, int size, int page) {
        String processedTitle = title.toLowerCase().trim().replaceAll(" +", " ");
        int startIndex = size * (page - 1);
        return jdbcBookRepository.findBooksByTitleAndAuthorAndGenre(processedTitle, author, genre, from, to, size, startIndex);
    }

    public int getAmountBooksByTitleAndAuthorAndGenre(String title, String author, String genre, Date from, Date to) {
        String processedTitle = title.toLowerCase().trim().replaceAll(" +", " ");
        return jdbcBookRepository.getAmountBooksByTitleAndAuthorAndGenre(processedTitle, author, genre, from, to);
    }

    public List<ViewBook> getFavouriteBooksByUserId(Long id, String sought, int cntBooks, int offset) {
        return jdbcBookRepository.getBooksByUserId(id, sought, cntBooks, offset, false, true,
                false, false,"", "");
    }

    public List<ViewBook> getReadingBooksByUserId(Long id, String sought, int cntBooks, int offset) {
        return jdbcBookRepository.getBooksByUserId(id, sought, cntBooks, offset, false, false,
                true, false, "", "");
    }

    public List<ViewBook> getReadBooksByUserId(Long id, String sought, int cntBooks, int offset) {
        return jdbcBookRepository.getBooksByUserId(id, sought, cntBooks, offset, true, false,
                false, false,"", "");
    }

    public Date getMinDateRelease() {
        return jdbcBookRepository.getMinDateRelease();
    }

    public Date getMaxDateRelease() {
        return jdbcBookRepository.getMaxDateRelease();
    }

    public int getAmountOfSearchResult(String title) {
        return jdbcBookRepository.getAmountOfSearchResult(title);
    }

    public boolean addReviewForUserBook(Review review) {
        // review.setReviewText(review.getReviewText().trim());
        review.setUserId(userRepository.getUserIdByName(review.getUserName()));
        return reviewRepository.addReviewForUserBook(review);
    }

    public boolean addBookToProfile(String userName, long bookId) {
        long userId = userRepository.getUserIdByName(userName);
        return jdbcBookRepository.addBookToProfile(userId, bookId);
    }


    public boolean approveReview(long reviewId) {
        return reviewRepository.approveReview(reviewId);
    }

    public boolean cancelReview(long reviewId) {
        return reviewRepository.cancelReview(reviewId);
    }

    public List<Review> getReviewsForApprove(int page, int itemPerPage){
        return reviewRepository.getReviewsForApprove(page, itemPerPage);
    }

    public boolean removeBookFromProfile(String userName, long bookId) {
        long userId = userRepository.getUserIdByName(userName);
        return jdbcBookRepository.removeBookFromProfile(userId, bookId);
    }

    public boolean checkBookInProfile(String userName, long bookId) {
        long userId = userRepository.getUserIdByName(userName);
        return jdbcBookRepository.checkBookInProfile(userId, bookId);
    }

    public List<ViewBook> getBooksByUserId(long userId, String sought, int cntBooks, boolean read, boolean favourite,
                                           boolean reading, boolean notSet, String sortBy, String order, int offset) {
        return jdbcBookRepository.getBooksByUserId(userId, sought, cntBooks, offset, read, favourite, reading, notSet,
                sortBy, order);
    }

    public void addBookBatchTo(Long userId, String shelf, List<Long> booksId) {
        jdbcBookRepository.addBookBatchTo(userId, shelf, booksId);
    }

    public void removeBookBatchFrom(long userId, String shelf, List<Long> booksId) {
        jdbcBookRepository.removeBookBatchFrom(userId, shelf, booksId);
    }

    public void removeBookBatch(long userId, List<Long> booksId) {
        jdbcBookRepository.removeBookBatch(userId, booksId);
    }
}
