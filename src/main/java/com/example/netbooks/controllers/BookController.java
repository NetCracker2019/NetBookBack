package com.example.netbooks.controllers;

import com.example.netbooks.dao.JdbcBookRepository;
import com.example.netbooks.models.Announcement;
import com.example.netbooks.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class BookController {
    @Autowired
    private JdbcBookRepository jdbcBookRepository;

    @RequestMapping(value="/home/books", method = RequestMethod.GET)
    public List<Book> getAllBooks() {
        return jdbcBookRepository.findAll();
    }

    @RequestMapping(value="/home/books/addBook", method = RequestMethod.POST, headers = {"Content-type=application/json"})
    public String add (@RequestBody Book book){
        return jdbcBookRepository.addBook(book);
    }

    @RequestMapping(value="/home/announcement", method = RequestMethod.GET)
    public List<Announcement> getAllAnnouncement() {
        return jdbcBookRepository.findAllAnnouncement();
    }
}