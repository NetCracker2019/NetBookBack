package com.example.netbooks.controllers;

import com.example.netbooks.dao.JdbcBookRepository;
import com.example.netbooks.models.Announcement;
import com.example.netbooks.models.Book;
import com.example.netbooks.services.BookService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class BookController {
    private final Logger logger = LogManager.getLogger(BookController.class);
    @Autowired
    private JdbcBookRepository jdbcBookRepository;
    @Autowired
    BookService bookService;

    @RequestMapping(value="/home/books/addBook", method = RequestMethod.POST, headers = {"Content-type=application/json"})
    public String add (@RequestBody Book book){
        return jdbcBookRepository.addBook(book);
    }

    @RequestMapping(value="/home/announcement", method = RequestMethod.GET)
    public List<Announcement> getAllAnnouncement() {
        return jdbcBookRepository.findAllAnnouncement();
    }
    @GetMapping("/home/books")
    public List<Book> getAllBooks() {
        logger.info("Books: "+bookService.getAllBooks());
        return bookService.getAllBooks();
    }
    @GetMapping("/home/find_books")
    public List<Book> getFoundBook(@RequestParam(value= "title", defaultValue = "lord") String title){
        logger.info("Books by title "+title+":  "+bookService.findBooks(title));
        return bookService.findBooks(title);
    }

    @GetMapping("/filteredBooks")
    public List<Book> getFilteredBooks
            (@RequestParam(value = "title", required = false, defaultValue = "") String title,
             @RequestParam(value = "author", required = false, defaultValue = "") String author,
             @RequestParam(value = "genre", required = false, defaultValue = "All") String genre,
             @RequestParam(value = "date1", required = false, defaultValue = "0001-01-01") String dateFrom,
             @RequestParam(value = "date2", required = false, defaultValue = "3000-01-01") String dateTo,
             @RequestParam(value = "page1", required = false, defaultValue = "0") int pageFrom,
             @RequestParam(value = "page2", required = false, defaultValue = "1000000") int pageTo
            ){
        return bookService.filterBooks(title, author, genre, dateFrom, dateTo, pageFrom, pageTo);
    }
}