package com.example.netbooks.controllers;


import com.example.netbooks.models.*;
import com.example.netbooks.services.BookService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://netbooksfront.herokuapp.com"})
@RequestMapping(value = "/approve-service")
public class ApproveControler {
    private final Logger logger = LogManager.getLogger(ProfileController.class);
    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public List<ViewAnnouncement> getUnApproveBooks() {
        logger.info(bookService.getViewUnApproveBooks());
        return bookService.getViewUnApproveBooks();
    }

    @PostMapping("/confirm-announcement")
    public String confirmAnnouncement (@RequestBody ViewAnnouncement announcement){
        logger.info(announcement);
        long id = announcement.getAnnouncmentId();
        logger.info(id);
        bookService.confirmAnnouncement(id);
        return "ok";
    }

    @PostMapping("/cancel-announcement")
    public String cancelAnnouncement (@RequestBody ViewAnnouncement announcement){
        logger.info(announcement);
        long id = announcement.getAnnouncmentId();
        logger.info(id);
        bookService.cancelAnnouncement(id);
        return "ok";
    }


}
