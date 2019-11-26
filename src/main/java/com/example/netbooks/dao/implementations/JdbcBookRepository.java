package com.example.netbooks.dao.implementations;

import com.example.netbooks.controllers.ProfileController;
import com.example.netbooks.dao.interfaces.BookRepository;
import com.example.netbooks.dao.mappers.BookRowMapper;
import com.example.netbooks.dao.mappers.EventMapper;
import com.example.netbooks.dao.mappers.ViewAnnouncementMapper;
import com.example.netbooks.dao.mappers.ViewBookMapper;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        return jdbcTemplate.queryForObject("SELECT COUNT (*) FROM announcement WHERE approved = true;", Integer.class);
    }
    @Override
    public List<Announcement> getPeaceAnnouncement(int page, int booksPerPage) {
        int startIndex = booksPerPage * (page - 1);
//        int amount = startIndex + booksPerPage;
//        logger.info(jdbcTemplate.query("SELECT * FROM announcement WHERE approved = true ORDER BY title LIMIT 5 OFFSET 1", this::mapRowToAnnouncement));
        return jdbcTemplate.query("SELECT * FROM announcement WHERE approved = true ORDER BY announcment_id LIMIT " + booksPerPage + " OFFSET " + startIndex, this::mapRowToAnnouncement);
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
    public List<ViewBook> findViewBooksByTitleOrAuthor(String titleOrAuthor) {
        titleOrAuthor = "%" + titleOrAuthor + "%";
        SqlParameterSource namedParameters = new MapSqlParameterSource("titleOrAuthor", titleOrAuthor);
        return namedJdbcTemplate.query(env.getProperty("findBooksByTitleOrAuthor"), namedParameters, viewBooksMapper);
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

    public List<ViewBook> findBooksByTitleAndGenre(String title, String genre, java.sql.Date from, java.sql.Date to) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("title", "%"+title+"%");
        namedParameters.addValue("genre", genre);
        namedParameters.addValue("from", from);
        namedParameters.addValue("to", to);
        return namedJdbcTemplate.query(env.getProperty("findBooksByTitleAndGenre") , namedParameters, viewBooksMapper);
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

    public List<ViewBook> findBooksByTitleAndAuthor(String title, String author, java.sql.Date from, java.sql.Date to) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("title", "%"+title+"%");
        namedParameters.addValue("author", author);
        namedParameters.addValue("from", from);
        namedParameters.addValue("to", to);
        return namedJdbcTemplate.query(env.getProperty("findBooksByTitleAndAuthor") , namedParameters, viewBooksMapper);
    }

    public List<ViewBook> findBooksByTitleAndDate(String title, java.sql.Date from, java.sql.Date to) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("title", "%"+title+"%");
        namedParameters.addValue("from", from);
        namedParameters.addValue("to", to);
        return namedJdbcTemplate.query(env.getProperty("findBooksByTitleAndDate") , namedParameters, viewBooksMapper);
    }

    public List<ViewBook> findBooksByTitleAndAuthorAndGenre(String title, String author, String genre, java.sql.Date from, java.sql.Date to) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("title", "%"+title+"%");
        namedParameters.addValue("author", author);
        namedParameters.addValue("genre", genre);
        namedParameters.addValue("from", from);
        namedParameters.addValue("to", to);
        return namedJdbcTemplate.query(env.getProperty("findBooksByTitleAuthorGenre") , namedParameters, viewBooksMapper);
    }


    @Override
    public List<Announcement> findAllAnnouncement() {
        return jdbcTemplate.query("SELECT * FROM announcement WHERE approved = true", this::mapRowToAnnouncement);
    }

    @Override
    public void addNewAnnouncement(Book book) {
        int id = jdbcTemplate.queryForObject("SELECT book_id FROM book WHERE title = '" + book.getTitle() + "'", Integer.class);
        jdbcTemplate.update("INSERT INTO announcement (announcement_book_id, user_id, approved, title, description, image_path) " +
                "VALUES(?, ?, ?, ?, ?, ?)",
                new Object[]{id ,15, false, book.getTitle(), book.getDescription(), book.getImagePath()});
    }

    @Override
    public String addAnnouncement(Book book) {
        addBook(book);
        addNewAnnouncement(book);
        return "Complete!";
    }

    @Override
    public String confirmAnnouncement(long announcementId) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("announcment_id", announcementId);
        jdbcTemplate.update("UPDATE announcement SET approved = true WHERE announcment_id = " + announcementId);
        return "ok";
    }

    @Override
    public String cancelAnnouncement(long announcementId) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("announcment_id", announcementId);
        jdbcTemplate.update("DELETE FROM announcement WHERE announcment_id = " + announcementId);
        return "ok";
    }

    @Override
    public String addRowIntoBookAuthor(Book book) {
        for (String item : book.getAuthor()) {
            //for (int i = 0; i < book.getGenre().size(); i++) {
            boolean isThisAuthorExist = jdbcTemplate.queryForObject("select exists(select 1 from author where fullname='" + item + "')", Boolean.class);
            if (isThisAuthorExist == false) {
                jdbcTemplate.update("insert into author (fullname) values (?)", new Object[] {item});
            }
            jdbcTemplate.update("insert into book_author values ((select book_id from book where title ='" + book.getTitle() + "'), \n" +
                        "\t\t   (select author_id from author where fullname ='" +  item + "'))");
        }
        return "Add Author";
    }

    public boolean checkIsExist(Book book) {
        return jdbcTemplate.queryForObject("select exists(select 1 from book where title='" + book.getTitle() + "')", Boolean.class);
    }

    @Override
    public String addBook(Book book) {
            jdbcTemplate.update("insert into book (title, likes, image_path, release_date, lang, pages, description, approved) " + "values(?, ?, ?, TO_DATE(?, 'yyyy-mm-dd'), ?, ?, ?, ?)",
                    new Object[]{book.getTitle(), 0, book.getImagePath(), book.getRelease_date(), book.getLanguage(), book.getPages(), book.getDescription(), false});
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

    private final class ShortViewBookMapper implements RowMapper<ViewBook> {
        @Override
        public ViewBook mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            ViewBook book = new ViewBook();
            book.setImagePath(resultSet.getString("image_path"));
            book.setTitle(resultSet.getString("title"));
            Array tmpArray = resultSet.getArray("authors");
            book.setAuthors((String[])tmpArray.getArray());
            return book;
        }
    }

    public List<ViewBook> getBooksByUserId(Long id, int cntBooks, int offset, String property) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("offset", offset);
            namedParams.put("cnt", cntBooks);
            namedParams.put("user_id", id);
            return namedJdbcTemplate.query(env.getProperty(property),
                    namedParams, new ShortViewBookMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


}
