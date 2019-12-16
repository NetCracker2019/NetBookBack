package com.example.netbooks.services;



import com.example.netbooks.dao.implementations.AchievementRepository;
import com.example.netbooks.dao.implementations.JdbcBookRepository;
import com.example.netbooks.dao.implementations.UserRepository;

import com.example.netbooks.dao.interfaces.AuthorRepository;
import com.example.netbooks.dao.interfaces.GenreRepository;
import com.example.netbooks.dao.interfaces.ReviewRepository;
import com.example.netbooks.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BookService {

    private final JdbcBookRepository jdbcBookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;
    private final AchievementService achievementService;

    @Autowired
    public BookService(JdbcBookRepository jdbcBookRepository,
                       GenreRepository genreRepository,
                       AuthorRepository authorRepository,
                       ReviewRepository reviewRepository,
                       UserRepository userRepository,
                       AchievementRepository achievementRepository,
                       AchievementService achievementService) {

        this.jdbcBookRepository = jdbcBookRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
        this.achievementService = achievementService;
    }

    public List<Book> getAllBooks(){
        return jdbcBookRepository.findAllBooks();
    }

    public List<ViewBook> getAllViewBooks(){
        return jdbcBookRepository.findAllViewBooks();
    }


    public List<ViewAnnouncement> getViewUnApproveBooks(int page, int offset) {
        return jdbcBookRepository.findViewUnApproveBooks(page, offset);
    }

    public int countReviews(boolean approved) {
        return reviewRepository.countReviews(approved);
    }

    public int countAnnouncement(boolean approved) {
        return jdbcBookRepository.countAnnouncement(approved);
    }

    public int countBooks() {
        return jdbcBookRepository.countBooks();
    }

    public List<Review> getReviewsForBook(int id){
        return reviewRepository.getReviewsByBookId(id);
    }

    public ViewBook getViewBookById(int id){
        return jdbcBookRepository.getBookById(id);
    }

    public ResponseEntity<Map> addBook(Book book) {

        Map<Object, Object> response = new HashMap<>();
        int userId = userRepository.getUserIdByLogin(book.getUser());

        if (jdbcBookRepository.checkIsDuplicate(book.getTitle(),book.getDescription()) == 0) {

            jdbcBookRepository.addBook(book, userId);
            genreRepository.addRowIntoBookGenre(book.getTitle(), book.getDescription(), book.getGenre());
            authorRepository.addRowIntoBookAuthor(book.getTitle(),book.getDescription(), book.getAuthor());

            response.put("status", "ok");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "This book exist");
            return ResponseEntity.ok(response);
        }
    }

    public ResponseEntity<Map> confirmAnnouncement(long announcementId) {
        jdbcBookRepository.confirmAnnouncement(announcementId);
        long userId = jdbcBookRepository.getUserIdByAnnouncementId(announcementId);
        try{
            UserAchievement userAchievement =
                    achievementRepository.checkUserAchievement(userId, "addedBooks");
            // TODO Send notif here
        } catch (NullPointerException e){
            e.getMessage();
        }
        Map<Object, Object> response = new HashMap<>();
        response.put("status", "ok");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map> cancelAnnouncement(long announcementId) {
        jdbcBookRepository.cancelAnnouncement(announcementId);

        Map<Object, Object> response = new HashMap<>();
        response.put("status", "ok");
        return ResponseEntity.ok(response);
    }

    public List<Event> calendarAnnouncement(String value, String userName) {
        int userId = userRepository.getUserIdByLogin(userName);
        if (value.equals("all")) {
            return jdbcBookRepository.getCalendarAllAnnouncement();
        } else {
            return jdbcBookRepository.getCalendarPersonalizeAnnouncement(userId);
        }
    }

    public List<Announcement> findAllAnnouncement() {
        return jdbcBookRepository.findAllAnnouncement();
    }

    public int getAmountOfAnnouncement() {
        return jdbcBookRepository.getAmountOfAnnouncement();
    }


    public int getAmountOfBook() {
        return jdbcBookRepository.getAmountOfBook();
    }

    public List<ViewBook> getPeaceBook(int page, int booksPerPage) {
        return jdbcBookRepository.getPeaceBook(page, booksPerPage);
    }

    public List<ViewBook> getPeaceAnnouncement(int page, int booksPerPage) {
        return jdbcBookRepository.getPeaceAnnouncement(page, booksPerPage);
    }

    public List<Genre> getAllGenres() {
        return genreRepository.getAllGenres();
    }

    public List<Author> getAllAuthors() {
        return authorRepository.getAllAuthors();
    }

    public List<ViewBook> getPeaceOfSearchBook(String searchString, int count, int offset){
        return jdbcBookRepository.getPeaceOfSearchBook(searchString, count, offset);
    }

    public List<Review> getPeaceOfReviewByBook(int bookId, int count, int offset){
        return reviewRepository.getPeaceOfReviewByBook(bookId, count, offset);
    }
    
    public Review getReviewById(long reviewId){
        return reviewRepository.getReviewById(reviewId);
    }

    public List<ViewBook> getPeaceOfBooks(int count, int offset){
        return jdbcBookRepository.getPeaceOfBook(count, offset);
    }

    public Page<ViewBook> getBooksByParameters(String title, String author, Integer genre, Date from, Date to, Pageable pageable) {
        List<ViewBook> books = Collections.emptyList();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startIndex = currentPage * pageSize;
        title = title.toLowerCase().trim().replaceAll(" +", " ");

        if (author == null && genre == null && from == null && to == null) {
            //log.info("search only with title param: {}", title);
            books = jdbcBookRepository.findViewBooksByTitle(title);
        } else if (author == null && genre == null && from != null && to != null) {
            //log.info("search with title and date params: {}, {}, {}", title, from, to);
            books = jdbcBookRepository.findBooksByTitleAndDate(title, from, to);
        } else if (author == null && genre != null && from != null && to != null) {
            //log.info("search with title, genre, date params params: {}, {}, {}, {}", title, genre, from ,to);
            books =  jdbcBookRepository.findBooksByTitleAndGenre(title, genre, from, to);
        } else if (author != null && genre == null && from != null && to != null) {
            //log.info("search with title, author, date params: {}, {}, {}, {}", title, author, from, to);
            books = jdbcBookRepository.findBooksByTitleAndAuthor(title, author, from, to);
        } else if (author != null && genre != null && from != null && to != null) {
            //log.info("search with title, author, genre, from, to params: {}, {}, {}, {}, {}", title, author, genre, from, to);
            books = jdbcBookRepository.findBooksByTitleAndAuthorAndGenre(title, author, genre, from, to);
        }

        List<ViewBook> result;
        if (books.size() < startIndex) {
            result = Collections.emptyList();
        } else {
            int toIndex = Math.min(startIndex + pageSize, books.size());
            result = books.subList(startIndex, toIndex);
        }

        return new PageImpl<>(result, PageRequest.of(currentPage, pageSize), books.size());
    }

    public List<ViewBook> getFavouriteBooksByUserId(Long id, String sought, int cntBooks, int offset) {
        return jdbcBookRepository.getBooksByUserId(id, sought, cntBooks, offset, false, true,
                false, false,BookParam.TITLE, Order.ASC);
    }

    public List<ViewBook> getReadingBooksByUserId(Long id, String sought, int cntBooks, int offset) {
        return jdbcBookRepository.getBooksByUserId(id, sought, cntBooks, offset, false, false,
                true, false, BookParam.TITLE, Order.ASC);
    }

    public List<ViewBook> getReadBooksByUserId(Long id, String sought, int cntBooks, int offset) {
        return jdbcBookRepository.getBooksByUserId(id, sought, cntBooks, offset, true, false,
                false, false,BookParam.TITLE, Order.ASC);
    }

    public Date getMinDateRelease() {
        return jdbcBookRepository.getMinDateRelease();
    }

    public Date getMaxDateRelease() {
        return jdbcBookRepository.getMaxDateRelease();
    }


    public boolean addReviewForUserBook(Review review) {
        review.setUserId(userRepository.getUserIdByLogin(review.getUserName()));
        review.setReviewText(review.getReviewText().trim().replaceAll(" +", " "));

        if (!userRepository.checkPersonIsUser(review.getUserId())) {
            review.setApproved(true);
            try{
                UserAchievement userAchievement =
                        achievementRepository.checkUserAchievement(review.getUserId(), "review");
                // TODO Send notif here
            } catch (NullPointerException e){
                e.getMessage();
            }
        }
        return reviewRepository.addReviewForUserBook(review);
    }


    public boolean addBookToProfile(String userName, long bookId) {
        long userId = userRepository.getUserIdByLogin(userName);
        boolean executionResult = jdbcBookRepository.addBookToProfile(userId, bookId);
        if (executionResult){
            try{
                UserAchievement userAchievement =
                    achievementRepository.checkUserAchievement(userId, "bookInProfile");
                // TODO Send notif here
            } catch (NullPointerException e){
                e.getMessage();
            }
            return true;
        }
        return false;
    }


    public boolean approveReview(long reviewId, long userId) {
        boolean executionResult = reviewRepository.approveReview(reviewId);
        if (executionResult){
            try{
                UserAchievement userAchievement =
                        achievementRepository.checkUserAchievement(userId, "review");
                // TODO Send notif here
            } catch (NullPointerException e){
                e.getMessage();
            }
            return true;
        }
        return false;

    }

    public boolean cancelReview(long reviewId) {
        return reviewRepository.cancelReview(reviewId);
    }

    public List<Review> getReviewsForApprove(int page, int itemPerPage){
        return reviewRepository.getReviewsForApprove(page, itemPerPage);
    }

    public boolean removeBookFromProfile(String userName, long bookId) {
        long userId = userRepository.getUserIdByLogin(userName);
        return jdbcBookRepository.removeBookFromProfile(userId, bookId);
    }

    public boolean checkBookInProfile(String userName, long bookId) {
        long userId = userRepository.getUserIdByLogin(userName);
        return jdbcBookRepository.checkBookInProfile(userId, bookId);
    }

    public Page<ViewBook> getSuggestions(String userName, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startIndex = currentPage * pageSize;
        long userId = userRepository.getUserIdByLogin(userName);

        List<ViewBook> books = jdbcBookRepository.getSuggestions(userId);

        List<ViewBook> result;
        if (books.size() < startIndex) {
            result = Collections.emptyList();
        } else {
            int toIndex = Math.min(startIndex + pageSize, books.size());
            result = books.subList(startIndex, toIndex);
        }

        return new PageImpl<>(result, PageRequest.of(currentPage, pageSize), books.size());
    }
  
    public List<ViewBook> getBooksByUserId(long userId, String sought, int cntBooks, boolean read, boolean favourite,
                                           boolean reading, boolean notSet, BookParam sortBy, Order order, int offset) {
        return jdbcBookRepository.getBooksByUserId(userId, sought, cntBooks, offset, read, favourite, reading, notSet,
                sortBy, order);
    }

    public void addBookBatchTo(Long userId, Shelf shelf, List<Long> booksId) {
        jdbcBookRepository.addBookBatchTo(userId, shelf, booksId);

        if(Shelf.Read.equals(shelf)){
            for (long bookId: booksId){
                achievementService.checkBookPatternAchievementsAndSendNotification(userId, bookId, "read");
            }
        }else if(Shelf.Favourite.equals(shelf)){
            for (long bookId: booksId){
                achievementService.checkBookPatternAchievementsAndSendNotification(userId, bookId, "fav");
            }
        }
    }

    public void removeBookBatchFrom(long userId, Shelf shelf, List<Long> booksId) {
        jdbcBookRepository.removeBookBatchFrom(userId, shelf, booksId);
    }

    public void removeBookBatch(long userId, List<Long> booksId) {
        jdbcBookRepository.removeBookBatch(userId, booksId);
    }
    public void likeBook(long bookId, String userLogin){
        long userId = userRepository.getUserIdByLogin(userLogin);
        jdbcBookRepository.likeBook(bookId, userId);
    }
    public void dislikeBook(long bookId, String userLogin){
        long userId = userRepository.getUserIdByLogin(userLogin);
        jdbcBookRepository.dislikeBook(bookId, userId);
    }
    public int checkLikedBook(long bookId, String userLogin){
        long userId = userRepository.getUserIdByLogin(userLogin);
        try {
            boolean liked = jdbcBookRepository.checkLickedBook(bookId, userId);
            if (liked) return 1;
            else return -1;
        }catch (EmptyResultDataAccessException e){
            return 0;
        }
    }
    public int likeReview(long reviewId, String userLogin){
        long userId = userRepository.getUserIdByLogin(userLogin);
        return reviewRepository.likeReview(reviewId, userId);
    }
    public int dislikeReview(long reviewId, String userLogin){
        long userId = userRepository.getUserIdByLogin(userLogin);
        return reviewRepository.dislikeReview(reviewId, userId);
    }
    public int checkLikedReview(long reviewId, String userLogin){
        long userId = userRepository.getUserIdByLogin(userLogin);
        return reviewRepository.checkLikedReview(reviewId, userId);
    }
    public int countReviewsForBook(long bookId) {
        return reviewRepository.countReviewsForBook(bookId);
    }
}
