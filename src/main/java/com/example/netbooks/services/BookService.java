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

    public List<ViewAnnouncement> getViewUnApproveBooks(){
        return jdbcBookRepository.findViewUnApproveBooks();
    }

    public int countReviews(boolean approved) {
        return reviewRepository.countReviews(approved);
    }

    public int countBooks(){
        return jdbcBookRepository.countBooks();
    }

    public List<Review> getReviewsForBook(int id){
        return reviewRepository.getReviewsByBookId(id);
    }

    public ViewBook getViewBookById(int id){
        return jdbcBookRepository.getBookById(id);
    }

    public ResponseEntity<Map> addBook(Book book) {

        int userId = userRepository.getUserIdByLogin(book.getUser());
        // TODO add validation of same title
        jdbcBookRepository.addBook(book, userId);
        genreRepository.addRowIntoBookGenre(book.getTitle(),book.getGenre());
        authorRepository.addRowIntoBookAuthor(book.getTitle(),book.getAuthor());
        int addedUserBook = jdbcBookRepository.countAddedBooksForUser(userId);
        long achvId = achievementService.getAchvIdByParameters(addedUserBook, "book-achievement", 1);

        UserAchievement userAchievement = achievementService.addAchievementToUser(achvId, userId);
        if (userAchievement != null) {
            // TODO Notification sending must be here.
        }

        Map<Object, Object> response = new HashMap<>();
        response.put("status", "ok");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map> confirmAnnouncement(long announcementId) {
        jdbcBookRepository.confirmAnnouncement(announcementId);

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

//    public String addAnnouncement(Book book) {
//        return jdbcBookRepository.addAnnouncement(book);
//    }

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
            log.info("search only with title param: {}", title);
            books = jdbcBookRepository.findViewBooksByTitleOrAuthor(title);
        } else if (author == null && genre == null && from != null && to != null) {
            log.info("search with title and date params: {}, {}, {}", title, from, to);
            books = jdbcBookRepository.findBooksByTitleAndDate(title, from, to);
        } else if (author == null && genre != null && from != null && to != null) {
            log.info("search with title, genre, date params params: {}, {}, {}, {}", title, genre, from ,to);
            books =  jdbcBookRepository.findBooksByTitleAndGenre(title, genre, from, to);
        } else if (author != null && genre == null && from != null && to != null) {
            log.info("search with title, author, date params: {}, {}, {}, {}", title, author, from, to);
            books = jdbcBookRepository.findBooksByTitleAndAuthor(title, author, from, to);
        } else if (author != null && genre != null && from != null && to != null) {
            log.info("search with title, author, genre, from, to params: {}, {}, {}, {}, {}", title, author, genre, from, to);
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
                false, false,"", "");
    }

    public List<ViewBook> getReadingBooksByUserId(Long id, String sought, int cntBooks, int offset) {
        return jdbcBookRepository.getBooksByUserId(id, sought, cntBooks, offset, false, false,
                true, false, "", "");
    }

    public List<ViewBook> getReadBooksByUserId(Long id, String sought, int cntBooks, int offset) {
        return jdbcBookRepository.getBooksByUserId(id, sought, cntBooks, offset, true, false,
                false, false,"", "");
    }

    public Date getMinDateRelease() {
        return jdbcBookRepository.getMinDateRelease();
    }

    public Date getMaxDateRelease() {
        return jdbcBookRepository.getMaxDateRelease();
    }

    public boolean addReviewForUserBook(Review review) {
        // review.setReviewText(review.getReviewText().trim());
        review.setUserId(userRepository.getUserIdByLogin(review.getUserName()));
        return reviewRepository.addReviewForUserBook(review);
    }


    public boolean addBookToProfile(String userName, long bookId) {
        long userId = userRepository.getUserIdByLogin(userName);
        boolean executionResult = jdbcBookRepository.addBookToProfile(userId, bookId);
        int booksForUser = jdbcBookRepository.countBooksForUser(userId);
        long achvId = achievementService.getAchvIdByParameters(booksForUser, "book", 10);

        UserAchievement userAchievement = achievementService.addAchievementToUser(achvId, userId);
        if (userAchievement != null) {
            // TODO Notification sending must be here.
        }
        return executionResult;
    }


    public boolean approveReview(long reviewId, long userId) {
        boolean executionResult = reviewRepository.approveReview(reviewId);
        int reviewsForUser = reviewRepository.countReviewsForUser(userId);
        long achvId = achievementService.getAchvIdByParameters(reviewsForUser, "review", 1);
        UserAchievement userAchievement = achievementService.addAchievementToUser(achvId, userId);
        if (userAchievement != null) {
            // TODO Notification sending must be here.
        }
        return executionResult;
    }

    public boolean cancelReview(long reviewId) {
        return reviewRepository.cancelReview(reviewId);
    }

//    public void likeReview(long reviewId){
//        reviewRepository.likeReview(reviewId);
//    }
//    public void likeBook(long bookId){
//        jdbcBookRepository.likeBook(bookId);
//    }
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

    public List<ViewBook> getSuggestions(String userName) {
        long userId = userRepository.getUserIdByLogin(userName);
        Map<String, Object> mapGenre = jdbcBookRepository.getFavouriteGenres(userId);
        Map<String, Object> mapAuthor = jdbcBookRepository.getFavouriteAuthors(userId);
        if (!mapGenre.isEmpty() && !mapAuthor.isEmpty()) {
            log.info("Map genre {}", mapGenre);
            log.info("Map author {}", mapAuthor);
            return jdbcBookRepository.getSuggestions(userId, (Integer) mapGenre.get("genre_id"), (Integer) mapAuthor.get("author_id"));
        } else {
            return Collections.emptyList();
        }
    }
  
    public List<ViewBook> getBooksByUserId(long userId, String sought, int cntBooks, boolean read, boolean favourite,
                                           boolean reading, boolean notSet, String sortBy, String order, int offset) {
        return jdbcBookRepository.getBooksByUserId(userId, sought, cntBooks, offset, read, favourite, reading, notSet,
                sortBy, order);
    }

    public void addBookBatchTo(Long userId, String shelf, List<Long> booksId) {
        if(shelf.equals("reading")){
            jdbcBookRepository.addBookBatchToReading(userId, booksId);
        }else if(shelf.equals("read")){
            jdbcBookRepository.addBookBatchToRead(userId, booksId);
            for (long bookId: booksId){
                boolean addedAuthorAchv = achievementRepository.check_achievement_author(userId, bookId, "read");
                if (addedAuthorAchv){
                    UserAchievement userAchievement = achievementRepository.getLastUserAchievement(userId);
                    // TODO Notification sending must be here.
                }
                boolean addedGenreAchv = achievementRepository.check_achievement_genre(userId, bookId, "read");
                if (addedGenreAchv){
                    UserAchievement userAchievement = achievementRepository.getLastUserAchievement(userId);
                    // TODO Notification sending must be here.
                }

            }
        }else {
            jdbcBookRepository.addBookBatchToFavourite(userId, booksId);
            for (long bookId: booksId){
                boolean addedAuthorAchv = achievementRepository.check_achievement_author(userId, bookId, "fav");
                if (addedAuthorAchv){
                    UserAchievement userAchievement = achievementRepository.getLastUserAchievement(userId);
                    // TODO Notification sending must be here.
                }
                boolean addedGenreAchv = achievementRepository.check_achievement_genre(userId, bookId, "fav");
                if (addedGenreAchv){
                    UserAchievement userAchievement = achievementRepository.getLastUserAchievement(userId);
                    // TODO Notification sending must be here.
                }

            }
        }
    }

    public void removeBookBatchFrom(long userId, String shelf, List<Long> booksId) {
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
        return jdbcBookRepository.checkLickedBook(bookId, userId);
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
}
