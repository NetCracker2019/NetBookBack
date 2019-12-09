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
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
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
    private DataSource dataSource;
    private final Environment env;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    private final Logger logger = LogManager.getLogger(ProfileController.class);
    private final RowMapper<ViewAnnouncement> viewAnnouncementMapper;
    private final RowMapper<ViewBook> viewBooksMapper;
    private final RowMapper<Event> eventMapper;
    private final RowMapper<Genre> genreNameMapper;
    private final RowMapper<Author> authorNameMapper;

    @Value("${getBookList}")
    private String getBookList;

    @Value("${getBookListByLikes}")
    private String getBookListByLikes;

    @Value("${getBookListDesc}")
    private String getBookListDesc;

    @Value("${getBookListByLikesDesc}")
    private String getBookListByLikesDesc;

    @Value("${addBookBatchToReading}")
    private String addBookBatchToReading;

    @Value("${addBookBatchToRead}")
    private String addBookBatchToRead;

    @Value("${addBookBatchToFavourite}")
    private String addBookBatchToFavourite;

    @Value("${removeBookBatchFromReading}")
    private String removeBookBatchFromReading;

    @Value("${removeBookBatchFromRead}")
    private String removeBookBatchFromRead;

    @Value("${removeBookBatchFromFavourite}")
    private String removeBookBatchFromFavourite;

    @Value("${addBookBatchToReading}")
    private String removeBookBatch;

    @Autowired
    public JdbcBookRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedJdbcTemplate, DataSource dataSource,
                              Environment env,
                              RowMapper<ViewAnnouncement> viewAnnouncementMapper,
                              ViewBookMapper viewBooksMapper,
                              RowMapper<Event> eventMapper,
                              RowMapper<Genre> genreNameMapper,
                              RowMapper<Author> authorNameMapper) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.env = env;
        this.viewAnnouncementMapper = viewAnnouncementMapper;
        this.viewBooksMapper = viewBooksMapper;
        this.eventMapper = eventMapper;
        this.genreNameMapper = genreNameMapper;
        this.dataSource = dataSource;
        this.authorNameMapper = authorNameMapper;
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
    public List<ViewBook> getPeaceAnnouncement(int page, int booksPerPage) {
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
    public List<ViewBook> findViewBooksByTitle(String title) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("title", "%" + title + "%");
        return namedJdbcTemplate.query(env.getRequiredProperty("findBooksByTitle"), namedParameters, viewBooksMapper);
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

    public List<ViewBook> findBooksByTitleAndGenre(String title, Integer genre, Date from, Date to) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("title", "%"+title+"%");
        namedParameters.addValue("genre", genre);
        namedParameters.addValue("from", from);
        namedParameters.addValue("to", to);
        return namedJdbcTemplate.query(env.getRequiredProperty("findBooksByTitleAndGenre") , namedParameters, viewBooksMapper);
    }

    @Override
    public List<Event> getCalendarAllAnnouncement() {
        logger.info(namedJdbcTemplate.query(env.getProperty("getCalendarAllAnnouncement"),eventMapper));
        return namedJdbcTemplate.query(env.getProperty("getCalendarAllAnnouncement"),eventMapper);
    }

    @Override
    public List<Event> getCalendarPersonalizeAnnouncement(int userId) {
        logger.info(userId);
        List<Event> result;
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("value", userId);

        List<Genre> favouriteGenre = namedJdbcTemplate.query(env.getProperty("getUsersFavouriteGenre"),namedParameters,genreNameMapper);
        List<Author> favouriteAuthor = namedJdbcTemplate.query(env.getProperty("getUsersFavouriteAuthor"),namedParameters,authorNameMapper);
        logger.info(favouriteGenre);
        logger.info(favouriteAuthor);
        String authors = "{";
        String genres = "{";

        for (Author item : favouriteAuthor) {
            logger.info(item.getFullName());

            authors += item.getFullName() + ", ";
        }
        authors = authors.replaceAll(", $", "");
        authors += "}";

        logger.info(authors);
        for (Genre item : favouriteGenre) {
            logger.info(item.getGenreName());
            genres += item.getGenreName() + ", ";
        }
        genres = genres.replaceAll(", $", "");
        genres += "}";


        namedParameters.addValue("authors", authors);
        namedParameters.addValue("genres", genres);
        logger.info(namedJdbcTemplate.query(env.getProperty("getPersonilizeAnnouncement"),namedParameters,eventMapper));

        return namedJdbcTemplate.query(env.getProperty("getPersonilizeAnnouncement"),namedParameters,eventMapper);
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

//    @Override
//    public void likeBook(long bookId) {
//        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
//        namedParameters.addValue("bookId", bookId);
//        namedJdbcTemplate.update(env.getProperty("likeBook"), namedParameters);
//    }

    @Override
    public int countBooksForUser(long userId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("userId", userId);
        return namedJdbcTemplate.queryForObject(env.getProperty("countBooksForUser"), namedParameters, Integer.class);
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<ViewBook> findBooksByTitleAndAuthor(String title, String author, Date from, Date to) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("title", "%"+title+"%");
        namedParameters.addValue("author", author);
        namedParameters.addValue("from", from);
        namedParameters.addValue("to", to);
        return namedJdbcTemplate.query(env.getRequiredProperty("findBooksByTitleAndAuthor") , namedParameters, viewBooksMapper);
    }

    public List<ViewBook> findBooksByTitleAndDate(String title, Date from, Date to) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("title", "%"+title+"%");
        namedParameters.addValue("from", from);
        namedParameters.addValue("to", to);
        return namedJdbcTemplate.query(env.getRequiredProperty("findBooksByTitleAndDate"), namedParameters, viewBooksMapper);
    }

    public List<ViewBook> findBooksByTitleAndAuthorAndGenre(String title, String author, Integer genre, Date from, Date to) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("title", "%"+title+"%");
        namedParameters.addValue("author", author);
        namedParameters.addValue("genre", genre);
        namedParameters.addValue("from", from);
        namedParameters.addValue("to", to);
        return namedJdbcTemplate.query(env.getRequiredProperty("findBooksByTitleAuthorGenre"), namedParameters, viewBooksMapper);
    }

    public Date getMinDateRelease() {
        return jdbcTemplate.queryForObject(env.getRequiredProperty("getMinDateRelease"), Date.class);
    }

    public Date getMaxDateRelease() {
        return jdbcTemplate.queryForObject(env.getRequiredProperty("getMaxDateRelease"), Date.class);
    }


    @Override
    public List<Announcement> findAllAnnouncement() {
        return jdbcTemplate.query("SELECT * FROM announcement WHERE approved = true", this::mapRowToAnnouncement);
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
        namedParams.put("sought", "%" + sought + "%");
        if("asc".equals(order)){
            if("title".equals(sortBy)){
                return namedJdbcTemplate.query(getBookList,
                        namedParams, new ShortViewBookMapper());
            }else{
                return namedJdbcTemplate.query(getBookListByLikes,
                        namedParams, new ShortViewBookMapper());
            }
        }else{
            if("title".equals(sortBy)){
                return namedJdbcTemplate.query(getBookListDesc,
                        namedParams, new ShortViewBookMapper());
            }else{
                return namedJdbcTemplate.query(getBookListByLikesDesc,
                        namedParams, new ShortViewBookMapper());
            }
        }
    }

    @Override
    public void addBookBatchToFavourite(Long userId, List<Long> booksId) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("booksId", booksId);
        namedParams.put("user_id", userId);
        namedJdbcTemplate.update(env.getProperty("addBookBatchToFavourite"), namedParams);
    }
    @Override
    public void addBookBatchToRead(Long userId, List<Long> booksId) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("booksId", booksId);
        namedParams.put("user_id", userId);
        namedJdbcTemplate.update(env.getProperty("addBookBatchToRead"), namedParams);
    }

    @Override
    public int countAddedBooksForUser(long userId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("userId", userId);
        return namedJdbcTemplate.queryForObject(env.getProperty("countAddedBooksForUser"), namedParameters, Integer.class);
    }

    @Override
    public void addBookBatchToReading(Long userId, List<Long> booksId) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("booksId", booksId);
        namedParams.put("user_id", userId);

        namedJdbcTemplate.update(env.getProperty("addBookBatchToReading"), namedParams);

    }
//    @Override
//    public void addBookBatchTo(Long userId, String shelf, List<Long> booksId) {
//        Map<String, Object> namedParams = new HashMap<>();
//        namedParams.put("booksId", booksId);
//        namedParams.put("user_id", userId);
//        if(shelf.equals("reading")){
//            namedJdbcTemplate.update(env.getProperty("addBookBatchToReading"), namedParams);
//        }else if(shelf.equals("read")){
//            namedJdbcTemplate.update(env.getProperty("addBookBatchToRead"), namedParams);
//        }else {
//            namedJdbcTemplate.update(env.getProperty("addBookBatchToFavourite"), namedParams);
//        }
//    }

    public List<ViewBook> getSuggestions(long userId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("userId", userId);
        return namedJdbcTemplate.query(env.getRequiredProperty("getSuggestions"), namedParameters, viewBooksMapper);
    }

    @Override
    public void removeBookBatchFrom(Long userId, String shelf, List<Long> booksId) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("booksId", booksId);
        namedParams.put("user_id", userId);
        if(shelf.equals("reading")){
            namedJdbcTemplate.update(removeBookBatchFromReading, namedParams);
        }else if(shelf.equals("read")){
            namedJdbcTemplate.update(removeBookBatchFromRead, namedParams);
        }else {
            namedJdbcTemplate.update(removeBookBatchFromFavourite, namedParams);
        }
    }
    @Override
    public void removeBookBatch(long userId, List<Long> booksId) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("booksId", booksId);
        namedParams.put("user_id", userId);
        namedJdbcTemplate.update(removeBookBatch, namedParams);
    }
    @Override
    public void likeBook(long bookId, long userId) {
        SimpleJdbcCall jdbcCall = new
                SimpleJdbcCall(dataSource).withFunctionName("like_book");

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("bookId", bookId)
                .addValue("userId", userId);
        jdbcCall.executeFunction(Boolean.class, in);

    }

    @Override
    public void dislikeBook(long bookId, long userId) {
        SimpleJdbcCall jdbcCall = new
                SimpleJdbcCall(dataSource).withFunctionName("dislike_book");

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("bookId", bookId)
                .addValue("userId", userId);
        jdbcCall.executeFunction(Boolean.class, in);
    }

    @Override
    public int checkLickedBook(long bookId, long userId) {
//        SimpleJdbcCall jdbcCall = new
//                SimpleJdbcCall(dataSource).withFunctionName("check_book_liked");
//
//        SqlParameterSource in = new MapSqlParameterSource()
//                .addValue("bookId", bookId)
//                .addValue("userId", userId);
//        return jdbcCall.executeFunction(Integer.class, in);
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("bookId", bookId);
        namedParams.put("userId", userId);
        Boolean likedExist = namedJdbcTemplate.queryForObject(env.getProperty("checkExistsLikedBookForUser"), namedParams, Boolean.class);
        if (!likedExist) {
            return 0;
        }
        Boolean liked = namedJdbcTemplate.queryForObject(env.getProperty("checkLikedBook"), namedParams, Boolean.class);
        if (liked) return 1;
        else return -1;
    }
}
