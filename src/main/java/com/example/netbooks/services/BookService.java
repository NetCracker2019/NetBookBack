package com.example.netbooks.services;

import com.example.netbooks.dao.GenreRepository;
import com.example.netbooks.dao.JdbcBookRepository;
import com.example.netbooks.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class BookService {
    @Autowired
    JdbcBookRepository jdbcBookRepository;
    @Autowired
    GenreRepository genreRepository;

    public List<Book> findBooks(String searchString){
        String processedString = String.join(" ", searchString.trim().replaceAll(" +", " "));
        System.out.println(processedString);
        List<Book> booksByTitle = jdbcBookRepository.findBooksByTitle(processedString);
        if (!booksByTitle.isEmpty())
            return booksByTitle;
        List<Book> booksByAuthor = jdbcBookRepository.findBooksByAuthor(processedString);
        return booksByAuthor;
    }
    public List<Book> getAllBooks(){
        return jdbcBookRepository.findAll();

    }
    public List<Book> filterBooks(String title, String author, String genre, String strDate1, String strDate2, int page1, int page2){
        String processedTitle = String.join(" ", title.trim().replaceAll(" +", " "));
        String processedAuthor = String.join(" ", author.trim().replaceAll(" +", " "));
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
}
