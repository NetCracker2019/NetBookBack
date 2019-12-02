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
    RowMapper viewAnnouncementMapper = new ViewAnnouncementMapper();
    private final Logger logger = LogManager.getLogger(ProfileController.class);
    private final RowMapper viewBooksMapper = new ViewBookMapper();
    private final RowMapper eventMapper = new EventMapper();
    private final RowMapper announcementMapper = new BookRowMapper();

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
    public List<ViewAnnouncement> findViewUnApproveBooks() {
        return jdbcTemplate.query(env.getProperty("getUnApprove"), viewAnnouncementMapper);
    }

    @Override
    public int getAmountOfAnnouncement() {
        return jdbcTemplate.queryForObject("SELECT COUNT (*) FROM book WHERE approved = true AND release_date >= now();", Integer.class);
    }
    @Override
    public List<Announcement> getPeaceAnnouncement(int page, int booksPerPage) {
        int startIndex = booksPerPage * (page - 1);
//        int amount = startIndex + booksPerPage;
//        logger.info(jdbcTemplate.query("SELECT * FROM announcement WHERE approved = true ORDER BY title LIMIT 5 OFFSET 1", this::mapRowToAnnouncement));
        return jdbcTemplate.query("SELECT book_id, title, description, image_path, release_date FROM book WHERE approved = true AND release_date >= now() ORDER BY book_id LIMIT " + booksPerPage + " OFFSET " + startIndex, announcementMapper);
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
    public List<Event> getCalendarPersonalizeAnnouncement() {
        return null;
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
    public String addBook(Book book, int userId) {
        jdbcTemplate.update("insert into book (title, likes, image_path, release_date, lang, pages, description, approved, user_id) " + "values(?, ?, ?, TO_DATE(?, 'yyyy-mm-dd'), ?, ?, ?, ?, ?)",
                    new Object[]{book.getTitle(), 0, book.getImagePath(), book.getRelease_date(), book.getLanguage(), book.getPages(), book.getDescription(), false, userId});
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

    public List<ViewBook> getBooksByUserId(Long id, String sought, int cntBooks, int offset, String property) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("offset", offset);
        namedParams.put("cnt", cntBooks);
        namedParams.put("user_id", id);
        namedParams.put("sought", "%" + sought + "%");
        return namedJdbcTemplate.query(env.getProperty(property),
                namedParams, new ShortViewBookMapper());
    }


}
