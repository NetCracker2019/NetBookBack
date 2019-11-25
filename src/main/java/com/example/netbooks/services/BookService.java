package com.example.netbooks.services;

import com.example.netbooks.dao.implementations.GenreRepositoryImpl;
import com.example.netbooks.dao.implementations.ReviewRepositoryImpl;
import com.example.netbooks.dao.interfaces.AuthorRepository;
import com.example.netbooks.dao.interfaces.GenreRepository;
import com.example.netbooks.dao.implementations.JdbcBookRepository;
import com.example.netbooks.dao.interfaces.ReviewRepository;
import com.example.netbooks.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class BookService {
    @Autowired
    JdbcBookRepository jdbcBookRepository;
    @Autowired
    GenreRepositoryImpl genreRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    ReviewRepository reviewRepository;

    public List<ViewBook> findBooks(String searchString){
        String processedString = searchString.toLowerCase().trim().replaceAll(" +", " ");
        List<ViewBook> books = jdbcBookRepository.findViewBooksByTitleOrAuthor(processedString);
        return books;
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
/////////////////////////////////////////////////////////////////////////////////////////////////////////
    public int getAmountOfBook() { return jdbcBookRepository.getAmountOfBook(); }

    public List<ViewBook> getPeaceBook(int page, int booksPerPage) {
        return jdbcBookRepository.getPeaceBook(page, booksPerPage);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
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
    public List<ViewBook> getBooksByTitleAndGenre(String title, String genre, java.sql.Date from, java.sql.Date to) {
        String processedTitle = title.toLowerCase().trim().replaceAll(" +", " ");
        return jdbcBookRepository.findBooksByTitleAndGenre(processedTitle, genre, from, to);
    }

    public List<ViewBook> getBooksByTitleAndAuthor(String title, String author, java.sql.Date from, java.sql.Date to) {
        String processedTitle = title.toLowerCase().trim().replaceAll(" +", " ");
        return jdbcBookRepository.findBooksByTitleAndAuthor(processedTitle, author, from, to);
    }

    public List<ViewBook> getBooksByTitleAndDate(String title, java.sql.Date from, java.sql.Date to) {
        String processedTitle = title.toLowerCase().trim().replaceAll(" +", " ");
        return jdbcBookRepository.findBooksByTitleAndDate(processedTitle, from, to);
    }

    public List<ViewBook> getBooksByTitleAndAuthorAndGenre(String title, String author, String genre, java.sql.Date from, java.sql.Date to) {
        String processedTitle = title.toLowerCase().trim().replaceAll(" +", " ");
        return jdbcBookRepository.findBooksByTitleAndAuthorAndGenre(processedTitle, author, genre, from, to);
    }

    public List<ViewBook> getFavouriteBooksByUserId(Long id, int cntBooks, int offset) {
        return jdbcBookRepository.getBooksByUserId(id, cntBooks, offset, "getFavouriteBooksByUserId");
    }

    public List<ViewBook> getReadingBooksByUserId(long id, int cntBooks, int offset) {
        return jdbcBookRepository.getBooksByUserId(id, cntBooks, offset, "getReadingBooksByUserId");
    }

    public List<ViewBook> getReadBooksByUserId(long id, int cntBooks, int offset) {
        return jdbcBookRepository.getBooksByUserId(id, cntBooks, offset, "getReadBooksByUserId");
    }
}
