package com.example.netbooks.controllers;

import com.example.netbooks.dao.implementations.JdbcBookRepository;
import com.example.netbooks.models.Announcement;
import com.example.netbooks.models.Book;
import com.example.netbooks.models.Review;
import com.example.netbooks.models.ViewBook;
import com.example.netbooks.services.BookService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
@RestController
@CrossOrigin(origins = "http://localhost:4200")
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
        return bookService.getAllBooks();
    }
    @GetMapping("/home/view-books")
    public List<ViewBook> getAllViewBooks() {
        logger.info("Books: "+bookService.getAllViewBooks());
        return bookService.getAllViewBooks();
    }
    @GetMapping("/home/find-books")
    public List<ViewBook> getFoundBook(@RequestParam("title") String title){
        logger.info("Books by title "+title+":  "+bookService.findBooks(title));
        return bookService.findBooks(title);
    }

    @GetMapping("/home/filter-books")
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
    @GetMapping("/home/search/{id}")
    public List<Review> getReviewForBook(@PathVariable("id") int bookId){
        logger.info(bookService.getReviewsForBook(bookId));
        return bookService.getReviewsForBook(bookId);
    }
    @GetMapping("/home/announcement/{id}")
    public List<Review> getReviewForAnnouncement(@PathVariable("id") int announcementId){
        return null;
    }
    @GetMapping("/home/find-book-by-id")
    public ViewBook getBookById(@RequestParam("id") int bookId){
        logger.info(bookService.getViewBookById(bookId));
        return bookService.getViewBookById(bookId);
    }
}
