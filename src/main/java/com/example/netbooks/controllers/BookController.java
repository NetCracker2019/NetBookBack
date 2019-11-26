package com.example.netbooks.controllers;

import com.example.netbooks.dao.implementations.JdbcBookRepository;
import com.example.netbooks.models.*;
import com.example.netbooks.services.BookService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/home/books")
    public List<ViewBook> getAllBooks() {
        return bookService.getAllViewBooks();
    }

    @PostMapping("/home/books/addBook")
    public String add (@RequestBody Book book){
        return jdbcBookRepository.addBook(book);
    }

    @PostMapping("/home/books/addAnnouncement")
    public String addAnnouncement (@RequestBody Book book){
        return jdbcBookRepository.addAnnouncement(book);
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
    @GetMapping("/home/view-books")
    public List<ViewBook> getPeaceViewBooks(@RequestParam("count") int count, @RequestParam("offset") int offset) {
        return bookService.getPeaceOfBooks(count, offset);
    }
    @GetMapping("/find-books")
    public List<ViewBook> getFoundBook(@RequestParam("title") String title,
                                       @RequestParam("size") int size,
                                       @RequestParam("page") int page){
        return bookService.findBooks(title, size, page);
    }

    @GetMapping("/amount-of-search-result")
    public int getAmountOfSearchResult(@RequestParam("title") String title) {
        return bookService.getAmountOfSearchResult(title);
    }

    @GetMapping("/home/search/{id}")
    public List<Review> getReviewForSearchBook(@PathVariable("id") int bookId, @RequestParam("count") int count, @RequestParam("offset") int offset ){
        return bookService.getPeaceOfReviewByBook(bookId, count, offset);
    }
    @GetMapping("/home/books/{id}")
    public List<Review> getReviewForBook(@PathVariable("id") int bookId, @RequestParam("count") int count, @RequestParam("offset") int offset ){
        return bookService.getPeaceOfReviewByBook(bookId, count, offset);
    }
    @GetMapping("/home/find-book-id")
    public ViewBook getBookById(@RequestParam("id") int bookId){
        logger.info(bookService.getViewBookById(bookId));
        return bookService.getViewBookById(bookId);
    }
    @GetMapping("home/books/amount")
    public int countBooks() {
        return bookService.countBooks();
    }

    @GetMapping("/filter-books-genre")
    public List<ViewBook> getBooksByTitleAndGenre(@RequestParam("title") String title,
                                                  @RequestParam("genre") String genre,
                                                  @RequestParam("from") Date from,
                                                  @RequestParam("to") Date to,
                                                  @RequestParam("size") int size,
                                                  @RequestParam("page") int page){
        return bookService.getBooksByTitleAndGenre(title, genre, from, to, size, page);
    }

    @GetMapping("/amount-filter-books-genre")
    public int getAmountBooksByTitleAndGenre(@RequestParam("title") String title,
                                             @RequestParam("genre") String genre,
                                             @RequestParam("from") Date from,
                                             @RequestParam("to") Date to){
        return bookService.getAmountBooksByTitleAndGenre(title, genre, from, to);
    }

    @GetMapping("/filter-books-author")
    public List<ViewBook> getBooksByTitleAndAuthor(@RequestParam("title") String title,
                                                   @RequestParam("author") String author,
                                                   @RequestParam("from") Date from,
                                                   @RequestParam("to") Date to,
                                                   @RequestParam("size") int size,
                                                   @RequestParam("page") int page){
        return bookService.getBooksByTitleAndAuthor(title, author, from, to, size, page);
    }

    @GetMapping("/amount-filter-books-author")
    public int getAmountBooksByTitleAndAuthor(@RequestParam("title") String title,
                                              @RequestParam("author") String author,
                                              @RequestParam("from") Date from,
                                              @RequestParam("to") Date to){
        return bookService.getAmountBooksByTitleAndAuthor(title, author, from, to);
    }

    @GetMapping("/filter-books")
    public List<ViewBook> getBooksByTitleAndDate(@RequestParam("title") String title,
                                                 @RequestParam("from") Date from,
                                                 @RequestParam("to") Date to,
                                                 @RequestParam("size") int size,
                                                 @RequestParam("page") int page){
        return bookService.getBooksByTitleAndDate(title, from, to, size, page);
    }

    @GetMapping("/amount-filter-books")
    public int getAmountBooksByTitleAndDate(@RequestParam("title") String title,
                                            @RequestParam("from") Date from,
                                            @RequestParam("to") Date to){
        return bookService.getAmountBooksByTitleAndDate(title, from, to);
    }

    @GetMapping("/filter-books-author-genre")
    public List<ViewBook> getBooksByTitleAndAuthorAndGenre(@RequestParam("title") String title,
                                                           @RequestParam("author") String author,
                                                           @RequestParam("genre") String genre,
                                                           @RequestParam("from") Date from,
                                                           @RequestParam("to") Date to,
                                                           @RequestParam("size") int size,
                                                           @RequestParam("page") int page){
        return bookService.getBooksByTitleAndAuthorAndGenre(title, author, genre, from, to, size, page);
    }

    @GetMapping("/amount-filter-books-author-genre")
    public int getAmountBooksByTitleAndAuthorAndGenre(@RequestParam(value = "title") String title,
                                                      @RequestParam(value = "author") String author,
                                                      @RequestParam(value = "genre") String genre,
                                                      @RequestParam(value = "from") Date from,
                                                      @RequestParam(value = "to") Date to){
        return bookService.getAmountBooksByTitleAndAuthorAndGenre(title, author, genre, from, to);
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
    public int countReviews(){
        return  bookService.countReviews();
    }
}
