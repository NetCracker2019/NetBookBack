package com.example.netbooks.controllers;


import com.example.netbooks.models.Review;
import com.example.netbooks.models.User;
import com.example.netbooks.models.ViewAnnouncement;
import com.example.netbooks.services.BookService;
import com.example.netbooks.services.NotificationService;
import com.example.netbooks.services.UserManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://netbooksfront.herokuapp.com"})
@RequestMapping(value = "/approve-service")
public class ApproveController {
    private final Logger logger = LogManager.getLogger(ApproveController.class);
    @Autowired
    private BookService bookService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserManager userManager;

    @GetMapping("/books")
    public List<ViewAnnouncement> getUnApproveBooks() {
        logger.info(bookService.getViewUnApproveBooks());
        return bookService.getViewUnApproveBooks();
    }

    @PostMapping("/confirm-announcement")
    public ResponseEntity confirmAnnouncement (@RequestBody ViewAnnouncement announcement){
        logger.info(announcement);
        long id = announcement.getAnnouncmentId();
        logger.info(id);
        return bookService.confirmAnnouncement(id);
    }

    @PostMapping("/cancel-announcement")
    public ResponseEntity cancelAnnouncement (@RequestBody ViewAnnouncement announcement){
        logger.info(announcement);
        long id = announcement.getAnnouncmentId();
        logger.info(id);
        return bookService.cancelAnnouncement(id);

    }

    @GetMapping("/reviews-for-approve")
    public List<Review> getReviewsForApprove(@RequestParam("page") int page,
                                             @RequestParam("itemPerPage") int offset){
        logger.info(bookService.getReviewsForApprove(page, offset));
        return bookService.getReviewsForApprove(page, offset);
    }
    @PostMapping("confirm-review")
    public boolean confirmReview(@RequestParam("reviewId") long reviewId, @RequestParam("userId") long userId){
//        Review review = bookService.getReviewById(reviewId);
//        User tmpUser = userManager.getUserById(review.getUserId());
//        List<User> friends = userManager.getFriendsByUsername(tmpUser.getLogin());
//        List<User>subscribers=userManager.getSubscribersByLogin(tmpUser.getLogin());
//        friends.addAll(subscribers);
//        notificationService.createAndSaveReviewNotif(review.getUserId(), friends, review.getBookId() , reviewId);
        return bookService.approveReview(reviewId, userId);
    }
    @PostMapping("cancel-review")
    public boolean cancelReview(@RequestParam("reviewId") long reviewId){
        return bookService.cancelReview(reviewId);
    }



}
