package com.example.netbooks.controllers;

import com.example.netbooks.dao.implementations.JdbcBookRepository;
import com.example.netbooks.models.*;
import com.example.netbooks.services.BookService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.http.HttpStatus;
import java.sql.Date;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/book")
    public ResponseEntity addBook (@RequestBody Book book){
        logger.info(book);
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
        logger.info("page {} booksPerPage {}",page, booksPerPage);
        return bookService.getPeaceBook(page,booksPerPage);
    }



    @GetMapping(value="/announcementListPeace")
    public List<Announcement> getPeaceAnnouncement(@RequestParam("page")int page, @RequestParam("booksPerPage")int booksPerPage) {
        logger.info("page {} booksPerPage {}",page, booksPerPage);
        return bookService.getPeaceAnnouncement(page,booksPerPage);
    }
    @GetMapping("/view-books")
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
    @DeleteMapping("/remove-book-profile")
    public boolean removeBookFromProfile(@RequestParam("userName") String userName, @RequestParam("bookId") int bookId){
        logger.info("Deleted book: "+userName+bookId);
        return bookService.removeBookFromProfile(userName, bookId);
    }
    @PutMapping("/like-book")
    public boolean likeBook(@RequestParam("bookId") long bookId){
        bookService.likeBook(bookId);
        logger.info("Book id : "+ bookId);
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
    public int countReviews(@RequestParam("approved") boolean approved){
        logger.info("Количетсво ревьюшек: "+bookService.countReviews(approved));
        return  bookService.countReviews(approved);
    }

    @GetMapping("/calendar-announcement")
    public List<Event> calendarAnnouncement(@RequestParam("value") String value, @RequestParam("userName") String userName) {
        logger.info(userName);
        return bookService.calendarAnnouncement(value, userName);
    }
}
