package com.example.netbooks.services;

import com.example.netbooks.dao.implementations.ReviewRepositoryImpl;
import com.example.netbooks.dao.interfaces.GenreRepository;
import com.example.netbooks.dao.implementations.JdbcBookRepository;
import com.example.netbooks.dao.interfaces.ReviewRepository;
import com.example.netbooks.models.Book;
import com.example.netbooks.models.Review;
import com.example.netbooks.models.ViewBook;
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
    GenreRepository genreRepository;
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
    public List<Book> filterBooks(String title, String author, String genre, String strDate1, String strDate2, int page1, int page2){
        String processedTitle = title.toLowerCase().trim().replaceAll(" +", " ");
        String processedAuthor = author.toLowerCase().trim().replaceAll(" +", " ");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-mm-dd").parse(strDate1);
            date2 = new SimpleDateFormat("yyyy-mm-dd").parse(strDate2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (page1 > 0 && page2>page1){
            return jdbcBookRepository.findBooksByFilter(processedTitle, processedAuthor, genre, date1, date2, page1, page2);
        }
        return null;
    }
    public List<Review> getReviewsForBook(int id){
        return reviewRepository.getReviewsByBookId(id);
    }
    public ViewBook getViewBookById(int id){
        return jdbcBookRepository.getBookById(id);
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
}
