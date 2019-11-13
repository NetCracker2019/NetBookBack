package com.example.netbooks.controllers;

import com.example.netbooks.dao.JdbcBookRepository;
import com.example.netbooks.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/home/books/addBook", method = RequestMethod.POST, headers = {"Content-type=application/json"})
public String add (@RequestBody Book book){
        return jdbcBookRepository.addBook(book);
        }

@RequestMapping(value="/home/announcement", method = RequestMethod.GET)
public List<Announcement> getAllAnnouncement() {
        return jdbcBookRepository.findAllAnnouncement();
        }
public class BookController {
    @Autowired
    private JdbcBookRepository jdbcBookRepository;

    @RequestMapping(value="/home/books/addBook", method = RequestMethod.POST, headers = {"Content-type=application/json"})
    public String add (@RequestBody Book book){
        return jdbcBookRepository.addBook(book);
    }

    @RequestMapping(value="/home/announcement", method = RequestMethod.GET)
    public List<Announcement> getAllAnnouncement() {
        return jdbcBookRepository.findAllAnnouncement();
    }
    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }
    @GetMapping("/find_books")
    public List<Book> getFoundBook(@RequestParam("searchString") String string){
        return bookService.findBooks(string);
    }
    @GetMapping("/filtered_books")
    public List<Book> getFilteredBooks
            (@RequestParam(value = "title", required = false, defaultValue = "") String title,
             @RequestParam(value = "author", required = false, defaultValue = "") String author,
             @RequestParam(value = "genre", required = false, defaultValue = "All") String genre,
             @RequestParam(value = "date1", required = false, defaultValue = "0001-01-01") String date1,
             @RequestParam(value = "date2", required = false, defaultValue = "3000-01-01") String date2,
             @RequestParam(value = "page1", required = false, defaultValue = "0") int page1,
             @RequestParam(value = "page2", required = false, defaultValue = "1000000") int page2
            ){
        return bookService.filterBooks(title, author, genre, date1, date2, page1, page2);
    }
}