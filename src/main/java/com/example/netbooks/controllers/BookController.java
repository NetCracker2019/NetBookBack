package com.example.netbooks.controllers;

import com.example.netbooks.dao.implementations.JdbcBookRepository;
import com.example.netbooks.models.*;
import com.example.netbooks.services.BookService;
import lombok.extern.slf4j.Slf4j;
import com.example.netbooks.services.NotificationService;
import com.example.netbooks.services.UserManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Map;
import java.sql.Date;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://netbooksfront.herokuapp.com"})
@RequestMapping("/book-service")
public class BookController {
    @Autowired
    private JdbcBookRepository jdbcBookRepository;
    final
    BookService bookService;
    @Autowired
    UserManager userManager;
    @Autowired
    NotificationService notificationService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public List<ViewBook> getAllBooks() {
        return bookService.getAllViewBooks();
    }

    @PostMapping("/book")
    public ResponseEntity addBook (@RequestBody Book book){
        log.info(book.toString());
        return bookService.addBook(book);
    }

//    @PostMapping("/books/addAnnouncement")
//    public String addAnnouncement (@RequestBody Book book){
//        return bookService.addAnnouncement(book);
//    }

    @GetMapping(value="/announcement")
    public List<Announcement> getAllAnnouncement() {
        return bookService.findAllAnnouncement();
    }

    @GetMapping(value="/amountOfAnnouncement")
    public int getAmountOfAnnouncement() {
        return bookService.getAmountOfAnnouncement();
    }



    @GetMapping(value="/amountOfBook")
    public int getAmountOfBook() {
        return bookService.getAmountOfBook();
    }


    @GetMapping(value="/bookListPeace")
    public List<ViewBook> getPeaceBook(@RequestParam("page")int page, @RequestParam("booksPerPage")int booksPerPage) {
        return bookService.getPeaceBook(page,booksPerPage);
    }

    @GetMapping(value="/announcementListPeace")
    public List<ViewBook> getPeaceAnnouncement(@RequestParam("page")int page, @RequestParam("booksPerPage")int booksPerPage) {
        return bookService.getPeaceAnnouncement(page,booksPerPage);
    }
    @GetMapping("/view-books")
    public List<ViewBook> getPeaceViewBooks(@RequestParam("count") int count, @RequestParam("offset") int offset) {
        return bookService.getPeaceOfBooks(count, offset);
    }
//    @GetMapping("/find-books")
//
//    public List<ViewBook> getFoundBook(@RequestParam("title") String title,
//                                       @RequestParam("size") int size,
//                                       @RequestParam("page") int page){
//        return bookService.findBooks(title, size, page);
//    }

    @PostMapping("/add-book-profile")
    public boolean addBookToProfile(@RequestParam("userName") String userName, @RequestParam("bookId") int bookId){
        log.info(userName+bookId);
        Thread notifThread = new Thread(() -> {
            long id = userManager.getUserIdByName(((UserDetails) SecurityContextHolder
                    .getContext().getAuthentication()
                    .getPrincipal()).getUsername());
            User tmpUser = userManager.getUserById(id);
            List<User> friends = userManager.getFriendsByUsername(tmpUser.getLogin());
            List<User> subscribers = userManager.getSubscribersByLogin(tmpUser.getLogin());
            friends.addAll(subscribers);
            for (User user : friends) {
                Notification notification = new Notification();
                notification.setNotifTypeId(2);
                notification.setUserId((int) userManager.getUserIdByName(user.getLogin()));
                notification.setFromUserId((int) (tmpUser.getUserId()));
                notification.setBookId(bookId);
                notificationService.addNotification(notification);
            }
        });
        notifThread.start();
        return bookService.addBookToProfile(userName, bookId);
    }
    @PostMapping("/add-review-user-book")
    public boolean addReviewForUserBook(@RequestBody Review review){
        return bookService.addReviewForUserBook(review);
    }
    @DeleteMapping("/remove-book-profile")
    public boolean removeBookFromProfile(@RequestParam("userName") String userName, @RequestParam("bookId") int bookId){
        return bookService.removeBookFromProfile(userName, bookId);
    }
    @PutMapping("/like-book")
    public boolean likeBook(@RequestParam("bookId") long bookId, @RequestParam("userLogin") String userLogin){
        bookService.likeBook(bookId, userLogin);
        return true;
    }
    @PutMapping("/dislike-book")
    public boolean dislikeBook(@RequestParam("bookId") long bookId, @RequestParam("userLogin") String userLogin){
        bookService.dislikeBook(bookId, userLogin);
        return true;
    }

    /**
     * Check book is liked, disliked or not liked by some user
     * @param bookId - book id
     * @param userLogin - user login
     * @return 0 if book is not liked/disliked by some user
     *         1 if book is liked
     *        -1 if book is disliked
     */
    @GetMapping("/check-liked-book")
    public int checkLikedBook(@RequestParam("bookId") long bookId, @RequestParam("userLogin") String userLogin){
        return bookService.checkLikedBook(bookId, userLogin);
    }
    @PutMapping("/like-review")
    public int likeReview(@RequestParam("reviewId") long reviewId, @RequestParam("userLogin") String userLogin){
        return bookService.likeReview(reviewId, userLogin);
    }
    @PutMapping("/dislike-review")
    public int dislikeReview(@RequestParam("reviewId") long reviewId, @RequestParam("userLogin") String userLogin){
        return bookService.dislikeReview(reviewId, userLogin);
    }
    @GetMapping("/check-liked-review")
    public int checkLikedReview(@RequestParam("reviewId") long reviewId, @RequestParam("userLogin") String userLogin){
        return bookService.checkLikedReview(reviewId, userLogin);
    }

    @GetMapping("/check-book-profile")
    public boolean checkBookInProfile(@RequestParam("userName") String userName, @RequestParam("bookId") int bookId) {
        return bookService.checkBookInProfile(userName, bookId);
    }

    @GetMapping("/search/{id}")
    public List<Review> getReviewForSearchBook(@PathVariable("id") int bookId, @RequestParam("count") int count, @RequestParam("offset") int offset ){
        return bookService.getPeaceOfReviewByBook(bookId, count, offset);
    }
    @GetMapping("/books/{id}")
    public List<Review> getReviewForBook(@PathVariable("id") int bookId, @RequestParam("count") int count, @RequestParam("offset") int offset ){
        return bookService.getPeaceOfReviewByBook(bookId, count, offset);
    }
    @GetMapping("/find-book-id")
    public ViewBook getBookById(@RequestParam("id") int bookId){
        return bookService.getViewBookById(bookId);
    }
    @GetMapping("/books/amount")
    public int countBooks() {
        return bookService.countBooks();
    }

    @GetMapping("/find-books")
    public Page<ViewBook> findBooks(@RequestParam(value = "title", required = false) String title,
                                    @RequestParam(value = "author", required = false) String author,
                                    @RequestParam(value = "genre", required = false) Integer genre,
                                    @RequestParam(value = "from", required = false) Date from,
                                    @RequestParam(value = "to", required = false) Date to,
                                    @RequestParam(value = "page") int page,
                                    @RequestParam(value = "size") int size) {
        log.info("author - {}, title - {}", author, title);
        return bookService.getBooksByParameters(title, author, genre, from, to, PageRequest.of(page, size));
    }

    @GetMapping("/min-date-release")
    public Date getMinDateRelease() {
        return bookService.getMinDateRelease();
    }

    @GetMapping("/max-date-release")
    public Date getMaxDateRelease() {
        return bookService.getMaxDateRelease();
    }

    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        return bookService.getAllGenres();
    }

    @GetMapping("/authors")
    public List<Author> getAllAuthors() {
        return bookService.getAllAuthors();
    }
    @GetMapping("/count-reviews")
    public int countReviews(@RequestParam("approved") boolean approved){
        return  bookService.countReviews(approved);
    }
    @GetMapping("/count-reviews-book")
    public int countReviewsForBook(@RequestParam("bookId") long bookId) {
        return bookService.countReviewsForBook(bookId);
    }

    @GetMapping("/calendar-announcement")
    public List<Event> calendarAnnouncement(@RequestParam("value") String value, @RequestParam("userName") String userName) {
        return bookService.calendarAnnouncement(value, userName);
    }

    @GetMapping("/suggestions")
    public Page<ViewBook> getSuggestions(@RequestParam("user") String userName,
                                         @RequestParam("page") int page,
                                         @RequestParam("size") int size) {
        return bookService.getSuggestions(userName, PageRequest.of(page, size));
    }
}
