package com.example.netbooks.controllers;

import com.example.netbooks.models.*;
import com.example.netbooks.services.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://netbooksfront.herokuapp.com"})
@RequestMapping("/book-service")
public class BookController {
    private BookService bookService;

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
        log.info("page {} booksPerPage {}",page, booksPerPage);
        return bookService.getPeaceBook(page,booksPerPage);
    }



    @GetMapping(value="/announcementListPeace")
    public List<ViewBook> getPeaceAnnouncement(@RequestParam("page")int page, @RequestParam("booksPerPage")int booksPerPage) {
        log.info("page {} booksPerPage {}",page, booksPerPage);
        return bookService.getPeaceAnnouncement(page,booksPerPage);
    }
    @GetMapping("/view-books")
    public List<ViewBook> getPeaceViewBooks(@RequestParam("count") int count, @RequestParam("offset") int offset) {
        return bookService.getPeaceOfBooks(count, offset);
    }

    //todo общий стиль
    @PostMapping("/add-book-profile")
    public boolean addBookToProfile(@RequestParam("userName") String userName, @RequestParam("bookId") int boolId){
        log.info(userName+boolId);
        return bookService.addBookToProfile(userName, boolId);
    }
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/add-review-user-book")
    public boolean addReviewForUserBook(@RequestBody Review review){
        log.info(review.toString());
        return bookService.addReviewForUserBook(review);
    }
    @DeleteMapping("/remove-book-profile")
    public boolean removeBookFromProfile(@RequestParam("userName") String userName, @RequestParam("bookId") int bookId){
        log.info("Deleted book: "+userName+bookId);
        return bookService.removeBookFromProfile(userName, bookId);
    }
    @PutMapping("/like-book")
    public boolean likeBook(@RequestParam("bookId") long bookId){
        bookService.likeBook(bookId);
        log.info("Book id : "+ bookId);
        return true;
    }
    @PutMapping("/like-review")
    public boolean likeReview(@RequestParam("reviewId") long reviewId){
        bookService.likeReview(reviewId);
        return true;
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
        log.info(bookService.getViewBookById(bookId).toString());
        return bookService.getViewBookById(bookId);
    }
    @GetMapping("/books/amount")
    public int countBooks() {
        return bookService.countBooks();
    }

    @GetMapping("/find-books")
    public Page<ViewBook> findBooks(@RequestParam(value = "title") String title,
                                   @RequestParam(value = "author", required = false) String author,
                                   @RequestParam(value = "genre", required = false) Integer genre,
                                   @RequestParam(value = "from", required = false) Date from,
                                   @RequestParam(value = "to", required = false) Date to,
                                   @RequestParam(value = "page") int page,
                                   @RequestParam(value = "size") int size) {
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
        log.info("Количетсво ревьюшек: "+bookService.countReviews(approved));
        return  bookService.countReviews(approved);
    }

    @GetMapping("/calendar-announcement")
    public List<Event> calendarAnnouncement(@RequestParam("value") String value, @RequestParam("userName") String userName) {
        log.info(userName);
        return bookService.calendarAnnouncement(value, userName);
    }

    @GetMapping("/suggestions")
    public Page<ViewBook> getSuggestions(@RequestParam("user") String userName,
                                         @RequestParam("page") int page,
                                         @RequestParam("size") int size) {
        return bookService.getSuggestions(userName, PageRequest.of(page, size));
    }
}
