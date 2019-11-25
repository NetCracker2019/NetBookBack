package com.example.netbooks.controllers;

import com.example.netbooks.dao.implementations.JdbcBookRepository;
import com.example.netbooks.models.*;
import com.example.netbooks.services.BookService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://netbooksfront.herokuapp.com"})
@RequestMapping("/book-service")
public class BookController {
    Logger logger = LogManager.getLogger(BookController.class);
    @Autowired
    private JdbcBookRepository jdbcBookRepository;
    final
    BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public List<ViewBook> getAllBooks() {
        return bookService.getAllViewBooks();
    }

    @PostMapping("/books/addBook")
    public String add (@RequestBody Book book){
        return jdbcBookRepository.addBook(book);
    }

    @PostMapping("/books/addAnnouncement")
    public String addAnnouncement (@RequestBody Book book){
        return jdbcBookRepository.addAnnouncement(book);
    }

    @GetMapping(value="/announcement")
    public List<Announcement> getAllAnnouncement() {
        return jdbcBookRepository.findAllAnnouncement();
    }
    @GetMapping(value="/amountOfAnnouncement")
    public int getAmountOfAnnouncement() {
        return jdbcBookRepository.getAmountOfAnnouncement();
    }

    @GetMapping(value="/announcementListPeace")
    public List<Announcement> getPeaceAnnouncement(@RequestParam("page")int page, @RequestParam("booksPerPage")int booksPerPage) {
        logger.info("page {} booksPerPage {}",page, booksPerPage);
        System.out.print(page + ' ' + booksPerPage);
        return jdbcBookRepository.getPeaceAnnouncement(page,booksPerPage);
    }
    @GetMapping("/view-books")
    public List<ViewBook> getPeaceViewBooks(@RequestParam("count") int count, @RequestParam("offset") int offset) {
        return bookService.getPeaceOfBooks(count, offset);
    }
    @GetMapping("/find-books")
    public List<ViewBook> getFoundBook(@RequestParam("title") String title){
        logger.info("Books by title "+title+":  "+bookService.findBooks(title));
        return bookService.findBooks(title);
    }
    @PostMapping("/add-book-profile")
    public boolean addBookToProfile(@RequestParam("userName") String userName, @RequestParam("bookId") int boolId){
        logger.info(userName+boolId);
        return bookService.addBookToProfile(userName, boolId);
    }
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/add-review-user-book")
    public boolean addReviewForUserBook(@RequestBody Review review){
        logger.info(review);
        return bookService.addReviewForUserBook(review);
    }
    @PostMapping("/remove-book-profile")
    public boolean removeBookFromProfile(@RequestParam("userName") String userName, @RequestParam("bookId") int bookId){
        logger.info("Deleted book: "+userName+bookId);
        return bookService.removeBookFromProfile(userName, bookId);
    }
    @GetMapping("/check-book-profile")
    public boolean checkBookInProfile(@RequestParam("userName") String userName, @RequestParam("bookId") int bookId) {
        return bookService.checkBookInProfile(userName, bookId);
    }

//    @GetMapping("/home/filter-books")
//    public List<Book> getFilteredBooks
//            (@RequestParam(value = "title", required = false, defaultValue = "") String title,
//             @RequestParam(value = "author", required = false, defaultValue = "") String author,
//             @RequestParam(value = "genre", required = false, defaultValue = "All") String genre,
//             @RequestParam(value = "date1", required = false, defaultValue = "0001-01-01") String dateFrom,
//             @RequestParam(value = "date2", required = false, defaultValue = "3000-01-01") String dateTo,
//             @RequestParam(value = "page1", required = false, defaultValue = "0") int pageFrom,
//             @RequestParam(value = "page2", required = false, defaultValue = "1000000") int pageTo
//            ){
//        return bookService.filterBooks(title, author, genre, dateFrom, dateTo, pageFrom, pageTo);
//    }
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
        logger.info(bookService.getViewBookById(bookId));
        return bookService.getViewBookById(bookId);
    }
    @GetMapping("/books/amount")
    public int countBooks() {
        return bookService.countBooks();
    }
    @GetMapping("/filter-books-genre")
    public List<ViewBook> getBooksByTitleAndGenre
            (@RequestParam(value = "title") String title,
             @RequestParam(value = "genre") String genre,
             @RequestParam(value = "from") Date from,
             @RequestParam(value = "to") Date to){
        return bookService.getBooksByTitleAndGenre(title, genre, from, to);
    }

    @GetMapping("/filter-books-author")
    public List<ViewBook> getBooksByTitleAndAuthor
            (@RequestParam(value = "title") String title,
             @RequestParam(value = "author") String author,
             @RequestParam(value = "from") Date from,
             @RequestParam(value = "to") Date to){
        logger.info("Books by title and author: " + title + ", " + author);
        return bookService.getBooksByTitleAndAuthor(title, author, from, to);
    }

    @GetMapping("/filter-books")
    public List<ViewBook> getBooksByTitleAndDate
            (@RequestParam(value = "title") String title,
             @RequestParam(value = "from") Date from,
             @RequestParam(value = "to") Date to){
        logger.info("Books by title and date: " + title + ", " + from + ", " + to);
        return bookService.getBooksByTitleAndDate(title, from, to);
    }

    @GetMapping("/filter-books-author-genre")
    public List<ViewBook> getBooksByTitleAndAuthorAndGenre
            (@RequestParam(value = "title") String title,
             @RequestParam(value = "author") String author,
             @RequestParam(value = "genre") String genre,
             @RequestParam(value = "from") Date from,
             @RequestParam(value = "to") Date to){
        logger.info("Books by title and date: " + title + ", " + from + ", " + to);
        return bookService.getBooksByTitleAndAuthorAndGenre(title, author, genre, from, to);
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
    public int countReviews(){
        return  bookService.countReviews();
    }

}
