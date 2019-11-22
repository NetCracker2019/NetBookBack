package com.example.netbooks.dao.implementations;

import com.example.netbooks.dao.interfaces.BookRepository;
import com.example.netbooks.dao.mappers.BookRowMapper;
import com.example.netbooks.dao.mappers.ViewBookMapper;
import com.example.netbooks.models.Announcement;
import com.example.netbooks.models.Book;
import com.example.netbooks.models.ShortBookDescription;
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

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:queries/book.properties")
@Repository
public class JdbcBookRepository implements BookRepository {
    @Autowired
    Environment env;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    RowMapper viewBooksMapper = new ViewBookMapper();


    @Override
    public List<ViewBook> findAllViewBooks() {
        return jdbcTemplate.query(env.getProperty("getAllBooks"), viewBooksMapper);
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
    public List<ViewBook> findViewBooksByTitleOrAuthor(String titleOrAuthor) {
        titleOrAuthor = "%" + titleOrAuthor + "%";
        SqlParameterSource namedParameters = new MapSqlParameterSource("titleOrAuthor", titleOrAuthor);
        return namedJdbcTemplate.query(env.getProperty("findBooksByTitleOrAuthor"), namedParameters, viewBooksMapper);
    }


    @Override
    public List<ViewBook> findViewBooksByFilter(String title, String author, String genre, Date date1, Date date2, int page1, int page2) {
        return null;
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
    public List<Book> findBooksByFilter(String title, String author, String genre, Date date1, Date date2, int page1, int page2) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("title", "%"+title+"%");
        namedParameters.addValue("author", "%"+author+"%");
        namedParameters.addValue("genre", genre);
        namedParameters.addValue("date1", date1);
        namedParameters.addValue("date2", date2);
        namedParameters.addValue("page1", page1);
        namedParameters.addValue("page2", page2);
        String sql = "All".equals(genre) ?
                "select * from book " +
                        "inner join book_author using (book_id) " +
                        "where lower(title) like :title " +
                        "and author_id in " +
                        "(select author_id from author where lower(firstname) like :author or lower(lastname) like :author) " +
                        "and release_date between :date1 and :date2 " +
                        "and pages between :page1 and :page2"
                :
                "select * from book " +
                        "inner join book_author using (book_id) " +
                        "inner join book_genre using (book_id) " +
                        "where lower(title) like :title " +
                        "and author_id in " +
                        "(select author_id from author where lower(firstname) like :author or lower(lastname) like :author) " +
                        "and genre_id in " +
                        "(select genre_id from genre where genre_name = :genre) " +
                        "and release_date between :date1 and :date2 " +
                        "and pages between :page1 and :page2";

        return namedJdbcTemplate.query(sql, namedParameters, new BookRowMapper());
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
    public String addBook(Book book) {
        int isThisBookExist = jdbcTemplate.queryForObject("select count(*) from book where title='" + book.getTitle() + "'", Integer.class);
        if(isThisBookExist == 0){
            jdbcTemplate.update("insert into book (title, likes, image_path, release_date, lang, pages, description, approved) " + "values(?, ?, ?, TO_DATE(?, 'yyyy-mm-dd'), ?, ?, ?, ?)",
                    new Object[] {book.getTitle(), 0, book.getImagePath(), book.getRelease_date(), book.getLanguage(), book.getPages(), book.getDescription(), false});

            String[] subStrAuthors = book.getAuthor().split(", ");
            for (int i = 0; i < subStrAuthors.length; i++) {
                int isThisAuthorExist = jdbcTemplate.queryForObject("select count(*) from author where fullname='" + subStrAuthors[i] + "'", Integer.class);
                if(isThisAuthorExist == 0){
                    jdbcTemplate.update("insert into author (fullname) values (?)", new Object[] {subStrAuthors[i]});
                }
                jdbcTemplate.update("insert into book_author values ((select book_id from book where title ='" + book.getTitle() + "'), \n" +
                        "\t\t   (select author_id from author where fullname ='" +  subStrAuthors[i] + "'))");
            }

            String[] subStrGenres = book.getGenre().split(", ");
            for (int i = 0; i < subStrGenres.length; i++) {
                int isThisGenreExist = jdbcTemplate.queryForObject("select count(*) from genre where genre_name='" + subStrGenres[i] + "'", Integer.class);
                if(isThisGenreExist == 0){
                    jdbcTemplate.update("insert into genre (genre_name) values (?)", new Object[] {subStrGenres[i]});
                }
                jdbcTemplate.update("insert into book_genre values ((select book_id from book where title ='" + book.getTitle() + "'), \n" +
                        "\t\t   (select genre_id from genre where genre_name ='" +  subStrGenres[i] + "'))");
            }
        }
        return "Complete!";
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
    private final class ShortDescriptionMapper implements RowMapper<ShortBookDescription> {
        @Override
        public ShortBookDescription mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            ShortBookDescription book = new ShortBookDescription();
            book.setImagePath(resultSet.getString("image_path"));
            book.setLikes(resultSet.getInt("likes"));
            book.setTitle(resultSet.getString("title"));
            Array tmpArray = resultSet.getArray("authors");
            book.setAuthors((String[])tmpArray.getArray());
            return book;
        }
    }

    public List<ShortBookDescription> getBooksByUserId(Long id, int cntBooks, int offset, String property) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("offset", offset);
            namedParams.put("cnt", cntBooks);
            namedParams.put("user_id", id);
            return namedJdbcTemplate.query(env.getProperty(property),
                    namedParams, new ShortDescriptionMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
