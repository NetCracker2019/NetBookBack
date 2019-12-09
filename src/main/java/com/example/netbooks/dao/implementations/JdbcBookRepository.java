package com.example.netbooks.dao.implementations;

import com.example.netbooks.controllers.ProfileController;
import com.example.netbooks.dao.interfaces.BookRepository;
import com.example.netbooks.dao.mappers.*;

import com.example.netbooks.models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.netbooks.models.Announcement;
import com.example.netbooks.models.Book;
import com.example.netbooks.models.ViewBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Array;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

@PropertySource("classpath:queries/book.properties")
@Repository
public class JdbcBookRepository implements BookRepository {
    @Autowired
    Environment env;
    //@Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Logger logger = LogManager.getLogger(ProfileController.class);
    private final RowMapper viewAnnouncementMapper = new ViewAnnouncementMapper();
    private final RowMapper viewBooksMapper = new ViewBookMapper();
    private final RowMapper eventMapper = new EventMapper();
    private final RowMapper announcementMapper = new BookRowMapper();
    private final RowMapper genreNameMapper = new GenreNameMapper();
    private final RowMapper authorNameMapper = new AuthorNameMapper();
    private final RowMapper bookIdMapper = new BookIdRowMapper();

    public JdbcBookRepository(DataSource dataSource) {
        namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<ViewBook> findAllViewBooks() {
        return jdbcTemplate.query(env.getProperty("getAllBooks"), viewBooksMapper);
    }
    public int countBooks(){
        return jdbcTemplate.queryForObject(env.getProperty("countBooks"), Integer.class);
    }

    @Override
    public List<ViewAnnouncement> findViewUnApproveBooks(int page, int offset) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("count", offset);
        namedParameters.addValue("offset", page);
        return namedJdbcTemplate.query(env.getProperty("getUnApprove"), namedParameters, viewAnnouncementMapper);
    }

    @Override
    public int getAmountOfAnnouncement() {
        return jdbcTemplate.queryForObject("SELECT COUNT (*) FROM book WHERE approved = true AND release_date >= now();", Integer.class);
    }
    @Override
    public List<Announcement> getPeaceAnnouncement(int page, int booksPerPage) {
        int startIndex = booksPerPage * (page - 1);
//        int amount = startIndex + booksPerPage;
        logger.info(jdbcTemplate.query("SELECT book_id, title, authors, likes, image_path, release_date, lang, pages, genres, description FROM view_book_list WHERE approved = true AND release_date >= now() ORDER BY book_id LIMIT " + booksPerPage + " OFFSET " + startIndex, viewBooksMapper));
        return jdbcTemplate.query("SELECT book_id, title, description, image_path, release_date, pages, genres, authors, likes, lang FROM view_book_list WHERE approved = true AND release_date >= now() ORDER BY book_id LIMIT " + booksPerPage + " OFFSET " + startIndex, viewBooksMapper);
    }
/////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public int getAmountOfBook() {
        return jdbcTemplate.queryForObject("SELECT COUNT (*) FROM view_book_list WHERE approved = true;", Integer.class);
    }

    @Override
    public List<ViewBook> getPeaceBook(int page, int booksPerPage) {
        int startIndex = booksPerPage * (page - 1);
//        int amount = startIndex + booksPerPage;
//        logger.info(jdbcTemplate.query("SELECT * FROM announcement WHERE approved = true ORDER BY title LIMIT 5 OFFSET 1", this::mapRowToAnnouncement));
        return jdbcTemplate.query("SELECT * FROM view_book_list WHERE approved = true ORDER BY title LIMIT " + booksPerPage + " OFFSET " + startIndex, viewBooksMapper);
    }
/////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public List<ViewBook> getPeaceOfSearchBook(String titleOrAuthor, int count, int offset) {
        titleOrAuthor = "%" + titleOrAuthor + "%";
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("titleOrAuthor", titleOrAuthor);
        namedParameters.addValue("count", count);
        namedParameters.addValue("offset", offset);
        return namedJdbcTemplate.query(env.getProperty("getPeaceOfSearchBooks"), namedParameters, viewBooksMapper);
    }

    @Override
    public List<ViewBook> getPeaceOfBook(int count, int offset) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("count", count);
        namedParameters.addValue("offset", offset);
        return namedJdbcTemplate.query(env.getProperty("getPeaceOfBooks"), namedParameters, viewBooksMapper);
    }

    @Override
    public boolean addBookToProfile(long userId, long bookId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("userId", userId);
        namedParameters.addValue("bookId", bookId);
        return namedJdbcTemplate.update(env.getProperty("addBookToProfile"), namedParameters) > 0;
    }

    @Override
    public boolean checkBookInProfile(long userId, long bookId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("userId", userId);
        namedParameters.addValue("bookId", bookId);
        return namedJdbcTemplate.queryForObject(env.getProperty("checkBookInProfile"), namedParameters, Integer.class) > 0;
    }

    @Override
    public boolean removeBookFromProfile(long userId, long bookId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("userId", userId);
        namedParameters.addValue("bookId", bookId);
        return namedJdbcTemplate.update(env.getProperty("removeBookFromProfile"), namedParameters) > 0;
    }


    @Override
    public List<ViewBook> findViewBooksByTitleOrAuthor(String titleOrAuthor, int size, int startIndex) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("titleOrAuthor", "%" + titleOrAuthor + "%");
        namedParameters.addValue("size", size);
        namedParameters.addValue("startIndex", startIndex);
        return namedJdbcTemplate.query(env.getProperty("findBooksByTitleOrAuthor"), namedParameters, viewBooksMapper);
    }

    public int getAmountOfSearchResult(String titleOrAuthor) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("titleOrAuthor", "%"+titleOrAuthor+"%");
        return namedJdbcTemplate.queryForObject(env.getProperty("getAmountOfSearchResult"), namedParameters, Integer.class);
    }

    @Override
    public ViewBook getBookById(int id) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        return (ViewBook) namedJdbcTemplate.query(env.getProperty("getBookById"), namedParameters, viewBooksMapper).get(0);
    }

    @Override
    public List<Book> findAllBooks() {
        return jdbcTemplate.query("select * from book", new BookRowMapper());
    }

    public List<ViewBook> findBooksByTitleAndGenre(String title, String genre, Date from, Date to, int size, int startIndex) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("title", "%"+title+"%");
        namedParameters.addValue("genre", genre);
        namedParameters.addValue("from", from);
        namedParameters.addValue("to", to);
        namedParameters.addValue("size", size);
        namedParameters.addValue("startIndex", startIndex);
        return namedJdbcTemplate.query(env.getProperty("findBooksByTitleAndGenre") , namedParameters, viewBooksMapper);
    }

    public int getAmountBooksByTitleAndGenre(String title, String genre, Date from, Date to) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("title", "%"+title+"%");
        namedParameters.addValue("genre", genre);
        namedParameters.addValue("from", from);
        namedParameters.addValue("to", to);
        return namedJdbcTemplate.queryForObject(env.getProperty("getAmountBooksByTitleAndGenre") , namedParameters, Integer.class);
    }

    @Override
    public List<Event> getCalendarAllAnnouncement() {
        logger.info(namedJdbcTemplate.query(env.getProperty("getCalendarAllAnnouncement"),eventMapper));
        return namedJdbcTemplate.query(env.getProperty("getCalendarAllAnnouncement"),eventMapper);
    }

    @Override
    public List<Event> getCalendarPersonalizeAnnouncement(int userId) {
        logger.info(userId);

        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("value", userId);
/////
        List<Book> favouriteGenreAndAuthorBooksId = namedJdbcTemplate.query(env.getProperty("getUsersFavouriteGenreAndAuthor"),namedParameters, bookIdMapper);
        logger.info("New feature request = " + favouriteGenreAndAuthorBooksId);
/////
//        List<Genre> favouriteGenre = namedJdbcTemplate.query(env.getProperty("getUsersFavouriteGenre"),namedParameters,genreNameMapper);
//        List<Author> favouriteAuthor = namedJdbcTemplate.query(env.getProperty("getUsersFavouriteAuthor"),namedParameters,authorNameMapper);
//        logger.info(favouriteGenre);
//        logger.info(favouriteAuthor);
//        String authors = "{";
//        String genres = "{";
//
//        for (Author item : favouriteAuthor) {
//            logger.info(item.getFullName());
//
//            authors += item.getFullName() + ", ";
//        }
//        authors = authors.replaceAll(", $", "");
//        authors += "}";
//
//        logger.info(authors);
//        for (Genre item : favouriteGenre) {
//            logger.info(item.getGenreName());
//            genres += item.getGenreName() + ", ";
//        }
//        genres = genres.replaceAll(", $", "");
//        genres += "}";
//
//
//        namedParameters.put("authors", authors);
//        namedParameters.put("genres", genres);
//        //logger.info(namedJdbcTemplate.query(env.getProperty("getPersonilizeAnnouncement"),namedParameters,eventMapper));

        List<Event> result = new ArrayList<>();
        for (Book item : favouriteGenreAndAuthorBooksId) {
            namedParameters.put("id", item.getBookId());
            //logger.info(namedJdbcTemplate.query(env.getProperty("getPersonilizeAnnouncementNew"), namedParameters, eventMapper));
            result.addAll(namedJdbcTemplate.query(env.getProperty("getPersonilizeAnnouncementNew"), namedParameters, eventMapper));
        }

        logger.info("Test of person book = " + result);

        return result;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public List<String> getFavouriteAuthor(int id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("value", id);
        //return namedJdbcTemplate.query(env.getProperty("getUsersFavouriteAuthorOrGenre") , namedParameters, viewBooksMapper);
        return null;
    }

    @Override
    public List<String> getFavouriteGenre(int id) {
        return null;
    }

    @Override
    public int countBooksForUser(long userId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("userId", userId);
        return namedJdbcTemplate.queryForObject(env.getProperty("countBooksForUser"), namedParameters, Integer.class);
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<ViewBook> findBooksByTitleAndAuthor(String title, String author, java.sql.Date from, java.sql.Date to, int size, int startIndex) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("title", "%"+title+"%");
        namedParameters.addValue("author", author);
        namedParameters.addValue("from", from);
        namedParameters.addValue("to", to);
        namedParameters.addValue("size", size);
        namedParameters.addValue("startIndex", startIndex);
        return namedJdbcTemplate.query(env.getProperty("findBooksByTitleAndAuthor") , namedParameters, viewBooksMapper);
    }

    public int getAmountBooksByTitleAndAuthor(String title, String author, Date from, Date to) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("title", "%"+title+"%");
        namedParameters.addValue("author", author);
        namedParameters.addValue("from", from);
        namedParameters.addValue("to", to);
        return namedJdbcTemplate.queryForObject(env.getProperty("getAmountBooksByTitleAndAuthor") , namedParameters, Integer.class);
    }

    public List<ViewBook> findBooksByTitleAndDate(String title, Date from, Date to, int size, int startIndex) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("title", "%"+title+"%");
        namedParameters.addValue("from", from);
        namedParameters.addValue("to", to);
        namedParameters.addValue("size", size);
        namedParameters.addValue("startIndex", startIndex);
        return namedJdbcTemplate.query(env.getProperty("findBooksByTitleAndDate"), namedParameters, viewBooksMapper);
    }

    public int getAmountBooksByTitleAndDate(String title, Date from, Date to) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("title", "%"+title+"%");
        namedParameters.addValue("from", from);
        namedParameters.addValue("to", to);
        return namedJdbcTemplate.queryForObject(env.getProperty("getAmountBooksByTitleAndDate"), namedParameters, Integer.class);
    }

    public List<ViewBook> findBooksByTitleAndAuthorAndGenre(String title, String author, String genre, Date from, Date to, int size, int startIndex) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("title", "%"+title+"%");
        namedParameters.addValue("author", author);
        namedParameters.addValue("genre", genre);
        namedParameters.addValue("from", from);
        namedParameters.addValue("to", to);
        namedParameters.addValue("size", size);
        namedParameters.addValue("startIndex", startIndex);
        return namedJdbcTemplate.query(env.getProperty("findBooksByTitleAuthorGenre"), namedParameters, viewBooksMapper);
    }

    public int getAmountBooksByTitleAndAuthorAndGenre(String title, String author, String genre, Date from, Date to) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("title", "%"+title+"%");
        namedParameters.addValue("author", author);
        namedParameters.addValue("genre", genre);
        namedParameters.addValue("from", from);
        namedParameters.addValue("to", to);
        return namedJdbcTemplate.queryForObject(env.getProperty("getAmountBooksByTitleAuthorGenre") , namedParameters, Integer.class);
    }

    public Date getMinDateRelease() {
        return jdbcTemplate.queryForObject("SELECT MIN(release_date) FROM view_book_list", Date.class);
    }

    public Date getMaxDateRelease() {
        return jdbcTemplate.queryForObject("SELECT MAX(release_date) FROM view_book_list", Date.class);
    }

    @Override
    public List<Announcement> findAllAnnouncement() {
        return jdbcTemplate.query("SELECT * FROM book WHERE approved = true AND release_date >= now()", this::mapRowToAnnouncement);
    }

//    @Override
//    public void addNewAnnouncement(Book book) {
//        int id = jdbcTemplate.queryForObject("SELECT book_id FROM book WHERE title = '" + book.getTitle() + "'", Integer.class);
//        jdbcTemplate.update("INSERT INTO announcement (announcement_book_id, user_id, approved, title, description, image_path) " +
//                "VALUES(?, ?, ?, ?, ?, ?)",
//                new Object[]{id ,15, false, book.getTitle(), book.getDescription(), book.getImagePath()});
//    }
//
//    @Override
//    public String addAnnouncement(Book book) {
//        addBook(book);
//        addNewAnnouncement(book);
//        return "Complete!";
//    }

    @Override
    public void confirmAnnouncement(long announcementId) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("announcment_id", announcementId);
        jdbcTemplate.update("UPDATE book SET approved = true WHERE book_id = " + announcementId);

    }

    @Override
    public void cancelAnnouncement(long announcementId) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("announcment_id", announcementId);
        jdbcTemplate.update("DELETE FROM book WHERE book_id = " + announcementId);

    }



    public boolean checkIsExist(Book book) {
        return jdbcTemplate.queryForObject("select exists(select 1 from book where title='" + book.getTitle() + "')", Boolean.class);
    }


    @Override
    public int checkIsDuplicate(String title, String description) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("title", title);
        namedParameters.addValue("description", description);
        return namedJdbcTemplate.queryForObject(env.getProperty("checkDuplicates"),namedParameters,Integer.class);
    }


    @Override
    public String addBook(Book book, int userId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("title", book.getTitle());
        namedParameters.addValue("likes", 0);
        namedParameters.addValue("image_path", book.getImagePath());
        namedParameters.addValue("release_date", book.getRelease_date());
        namedParameters.addValue("lang", book.getLanguage());
        namedParameters.addValue("pages", book.getPages());
        namedParameters.addValue("description", book.getDescription());
        namedParameters.addValue("approved", false);
        namedParameters.addValue("user_id", userId);
        namedJdbcTemplate.update(env.getProperty("addBook"),namedParameters);
        //jdbcTemplate.update("insert into book (title, likes, image_path, release_date, lang, pages, description, approved, user_id) " + "values(?, ?, ?, TO_DATE(?, 'yyyy-mm-dd'), ?, ?, ?, ?, ?)",
        //            new Object[]{book.getTitle(), 0, book.getImagePath(), book.getRelease_date(), book.getLanguage(), book.getPages(), book.getDescription(), false, userId});
        return "Complete adding!";
    }





    private Announcement mapRowToAnnouncement(ResultSet resultSet, int i) throws SQLException {
        return new Announcement(
                resultSet.getInt("announcment_id"),
                resultSet.getInt("announcement_book_id"),
                resultSet.getInt("user_id"),
                resultSet.getBoolean("approved"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getString("image_path"));
    }
    @Override
    public List<ViewBook> getBooksByUserId(Long id, String sought, int cntBooks, int offset, boolean read,
                                           boolean favourite, boolean reading, boolean notSet, String sortBy, String order) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("offset", offset);
        namedParams.put("cnt", cntBooks);
        namedParams.put("user_id", id);
        namedParams.put("read", read);
        namedParams.put("favourite", favourite);
        namedParams.put("reading", reading);
        namedParams.put("not_set", notSet);
        //namedParams.put("user_id", id);
        //namedParams.put("user_id", id);
        namedParams.put("sought", "%" + sought + "%");
        return namedJdbcTemplate.query(env.getProperty("getBookList"),
                namedParams, new ShortViewBookMapper());
    }
    @Override
    public void addBookBatchTo(Long userId, String shelf, List<Long> booksId) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("booksId", booksId);
        namedParams.put("user_id", userId);
        if(shelf.equals("reading")){
            namedJdbcTemplate.update(env.getProperty("addBookBatchToReading"), namedParams);
        }else if(shelf.equals("read")){
            namedJdbcTemplate.update(env.getProperty("addBookBatchToRead"), namedParams);
        }else {
            namedJdbcTemplate.update(env.getProperty("addBookBatchToFavourite"), namedParams);
        }
    }

    @Override
    public void removeBookBatchFrom(Long userId, String shelf, List<Long> booksId) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("booksId", booksId);
        namedParams.put("user_id", userId);
        if(shelf.equals("reading")){
            namedJdbcTemplate.update(env.getProperty("removeBookBatchFromReading"), namedParams);
        }else if(shelf.equals("read")){
            namedJdbcTemplate.update(env.getProperty("removeBookBatchFromRead"), namedParams);
        }else {
            namedJdbcTemplate.update(env.getProperty("removeBookBatchFromFavourite"), namedParams);
        }
    }
    @Override
    public void removeBookBatch(long userId, List<Long> booksId) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("booksId", booksId);
        namedParams.put("user_id", userId);
        namedJdbcTemplate.update(env.getProperty("removeBookBatch"), namedParams);
    }

    @Override
    public int countAnnouncement(boolean approved){
        SqlParameterSource namedParameters = new MapSqlParameterSource("approved", approved);
        return namedJdbcTemplate.queryForObject(env.getProperty("countUnApproveAnnouncement"), namedParameters, Integer.class);
    }

}
