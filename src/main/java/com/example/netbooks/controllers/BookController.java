package com.example.netbooks.controllers;

import com.example.netbooks.dao.JdbcBookRepository;
import com.example.netbooks.models.Announcement;
import com.example.netbooks.models.Book;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://netbooksfront.herokuapp.com"})
@RequestMapping(value = "/book-service")
public class BookController {
    private final Logger logger = LogManager.getLogger(AuthenticationController.class);
    @Autowired
    private JdbcBookRepository jdbcBookRepository;

    @GetMapping(value="/home/books")
    public List<Book> getAllBooks() {
        return jdbcBookRepository.findAll();
    }

    @PostMapping("/home/books/addBook")
    public String add (@RequestBody Book book){
        return jdbcBookRepository.addBook(book);
    }

    @GetMapping(value="/home/announcement")
    public List<Announcement> getAllAnnouncement() {
        return jdbcBookRepository.findAllAnnouncement();
    }

    @GetMapping(value="/home/amountOfAnnouncement")
    public int getAmountOfAnnouncement() {
        return jdbcBookRepository.getAmountOfAnnouncement();
    }

    @GetMapping(value="/home/announcementListPeace")
    public List<Announcement> getPeaceAnnouncement(@RequestParam("page")int page, @RequestParam("booksPerPage")int booksPerPage) {
        logger.info("page {} booksPerPage {}",page, booksPerPage);
        System.out.print(page + ' ' + booksPerPage);
        return jdbcBookRepository.getPeaceAnnouncement(page,booksPerPage);
    }
}
