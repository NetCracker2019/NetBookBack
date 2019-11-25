package com.example.netbooks.services;

import com.example.netbooks.dao.implementations.ReviewRepositoryImpl;
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
    @Autowired
    JdbcBookRepository jdbcBookRepository;
    @Autowired
    GenreRepository genreRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    ReviewRepository reviewRepository;

    public List<ViewBook> findBooks(String searchString, int size, int page){
        String processedString = searchString.toLowerCase().trim().replaceAll(" +", " ");
        int startIndex = size * (page - 1);
        return jdbcBookRepository.findViewBooksByTitleOrAuthor(processedString, size, startIndex);
    }
    public List<Book> getAllBooks(){
        return jdbcBookRepository.findAllBooks();

    }
    public List<ViewBook> getAllViewBooks(){
        return jdbcBookRepository.findAllViewBooks();

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

    public List<ViewBook> getFavouriteBooksByUserId(Long id, int cntBooks, int offset) {
        return jdbcBookRepository.getBooksByUserId(id, cntBooks, offset, "getFavouriteBooksByUserId");
    }

    public List<ViewBook> getReadingBooksByUserId(long id, int cntBooks, int offset) {
        return jdbcBookRepository.getBooksByUserId(id, cntBooks, offset, "getReadingBooksByUserId");
    }

    public List<ViewBook> getReadBooksByUserId(long id, int cntBooks, int offset) {
        return jdbcBookRepository.getBooksByUserId(id, cntBooks, offset, "getReadBooksByUserId");
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
}
