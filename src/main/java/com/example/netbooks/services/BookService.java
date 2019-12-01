package com.example.netbooks.services;

import com.example.netbooks.dao.implementations.JdbcBookRepository;
import com.example.netbooks.dao.implementations.UserRepository;
import com.example.netbooks.dao.interfaces.AuthorRepository;
import com.example.netbooks.dao.interfaces.GenreRepository;
import com.example.netbooks.dao.interfaces.ReviewRepository;
import com.example.netbooks.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class BookService {
    final JdbcBookRepository jdbcBookRepository;
    final GenreRepository genreRepository;
    final AuthorRepository authorRepository;
    final ReviewRepository reviewRepository;
    final UserRepository userRepository;

    @Autowired
    public BookService(JdbcBookRepository jdbcBookRepository,
                       GenreRepository genreRepository,
                       AuthorRepository authorRepository,
                       ReviewRepository reviewRepository,
                       UserRepository userRepository) {
        this.jdbcBookRepository = jdbcBookRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    public List<Book> getAllBooks(){
        return jdbcBookRepository.findAllBooks();
    }

    public List<ViewBook> getAllViewBooks(){
        return jdbcBookRepository.findAllViewBooks();
    }

    public List<ViewAnnouncement> getViewUnApproveBooks(){
        return jdbcBookRepository.findViewUnApproveBooks();
    }

    public int countReviews(){
        return reviewRepository.countReviews();
    }

    public int countBooks(){
        return jdbcBookRepository.countBooks();
    }

    public List<Review> getReviewsForBook(int id){
        return reviewRepository.getReviewsByBookId(id);
    }

    public ViewBook getViewBookById(int id){
        return jdbcBookRepository.getBookById(id);
    }

    public String addBook(Book book, String value)
    {
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
        }else {
            return jdbcBookRepository.getCalendarPersonalizeAnnouncement();
        }
    }

    public String addAnnouncement(Book book) { return jdbcBookRepository.addAnnouncement(book);}

    public List<Announcement> findAllAnnouncement() { return jdbcBookRepository.findAllAnnouncement(); }

    public int getAmountOfAnnouncement() { return jdbcBookRepository.getAmountOfAnnouncement(); }

    public int getAmountOfBook() { return jdbcBookRepository.getAmountOfBook(); }

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

    public List<ViewBook> getPeaceOfSearchBook(String searchString, int count, int offset){
        return jdbcBookRepository.getPeaceOfSearchBook(searchString, count, offset);
    }

    public List<Review> getPeaceOfReviewByBook(int bookId, int count, int offset){
        return reviewRepository.getPeaceOfReviewByBook(bookId, count, offset);
    }

    public List<ViewBook> getPeaceOfBooks(int count, int offset){
        return jdbcBookRepository.getPeaceOfBook(count, offset);
    }

    public Page<ViewBook> getBooksByParameters(String title, String author, String genre, Date from, Date to, Pageable pageable) {
        List<ViewBook> books = Collections.emptyList();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startIndex = currentPage * pageSize;
        title = title.toLowerCase().trim().replaceAll(" +", " ");

        if (author == null && genre == null && from == null && to == null) {
            log.info("search only with title param: {}", title);
            books = jdbcBookRepository.findViewBooksByTitleOrAuthor(title);
        } else if (author == null && genre == null && from != null && to != null) {
            log.info("search with title and date params: {}, {}, {}", title, from, to);
            books = jdbcBookRepository.findBooksByTitleAndDate(title, from, to);
        } else if (author == null && genre != null && from != null && to != null) {
            log.info("search with title, genre, date params params: {}, {}, {}, {}", title, genre, from ,to);
            books =  jdbcBookRepository.findBooksByTitleAndGenre(title, genre, from, to);
        } else if (author != null && genre == null && from != null && to != null) {
            log.info("search with title, author, date params: {}, {}, {}, {}", title, author, from, to);
            books = jdbcBookRepository.findBooksByTitleAndAuthor(title, author, from, to);
        } else if (author != null && genre != null && from != null && to != null) {
            log.info("search with title, author, genre, from, to params: {}, {}, {}, {}, {}", title, author, genre, from, to);
            books = jdbcBookRepository.findBooksByTitleAndAuthorAndGenre(title, author, genre, from, to);
        }

        List<ViewBook> result;
        if (books.size() < startIndex) {
            result = Collections.emptyList();
        } else {
            int toIndex = Math.min(startIndex + pageSize, books.size());
            result = books.subList(startIndex, toIndex);
        }

        return new PageImpl<>(result, PageRequest.of(currentPage, pageSize), books.size());
    }

    public List<ViewBook> getFavouriteBooksByUserId(Long id, String sought, int cntBooks, int offset) {
        return jdbcBookRepository.getBooksByUserId(id, sought, cntBooks, offset, "getFavouriteBooksByUserId");
    }

    public List<ViewBook> getReadingBooksByUserId(long id, String sought, int cntBooks, int offset) {
        return jdbcBookRepository.getBooksByUserId(id, sought, cntBooks, offset, "getReadingBooksByUserId");
    }

    public List<ViewBook> getReadBooksByUserId(long id, String sought, int cntBooks, int offset) {
        return jdbcBookRepository.getBooksByUserId(id, sought, cntBooks, offset, "getReadBooksByUserId");
    }

    public Date getMinDateRelease() {
        return jdbcBookRepository.getMinDateRelease();
    }

    public Date getMaxDateRelease() {
        return jdbcBookRepository.getMaxDateRelease();
    }

    public boolean addReviewForUserBook(Review review) {
        // review.setReviewText(review.getReviewText().trim());
        review.setUserId(userRepository.getUserIdByName(review.getUserName()));
        return reviewRepository.addReviewForUserBook(review);
    }
    public boolean addBookToProfile(String userName, long bookId){
        long userId = userRepository.getUserIdByName(userName);
        return jdbcBookRepository.addBookToProfile(userId, bookId);
    }
    public boolean approveReview(long reviewId) {
        return reviewRepository.approveReview(reviewId);
    }
    public boolean cancelReview(long reviewId) {
        return reviewRepository.cancelReview(reviewId);
    }

    public boolean removeBookFromProfile(String userName, long bookId){
        long userId = userRepository.getUserIdByName(userName);
        return jdbcBookRepository.removeBookFromProfile(userId, bookId);
    }
    public boolean checkBookInProfile(String userName, long bookId){
        long userId = userRepository.getUserIdByName(userName);
        return jdbcBookRepository.checkBookInProfile(userId, bookId);
    }
}
