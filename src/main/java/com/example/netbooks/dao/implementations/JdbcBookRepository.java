package com.example.netbooks.dao.implementations;

import com.example.netbooks.dao.interfaces.BookRepository;
import com.example.netbooks.dao.mappers.BookRowMapper;
import com.example.netbooks.dao.mappers.ViewBookMapper;
import com.example.netbooks.models.Announcement;
import com.example.netbooks.models.Book;
import com.example.netbooks.models.ViewBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@PropertySource("classpath:queries/book.properties")
@Repository
public class JdbcBookRepository implements BookRepository {
    @Autowired
    Environment env;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    RowMapper viewBooksMapper = new ViewBookMapper();


    @Override
    public List<ViewBook> findAllViewBooks() {
        return jdbcTemplate.query(env.getProperty("getAllBooks"), viewBooksMapper);
    }

    @Override
    public int getAmountOfAnnouncement() {
        return jdbcTemplate.queryForObject("SELECT COUNT (*) FROM announcement;", Integer.class);
    }
    @Override
    public List<Announcement> getPeaceAnnouncement(int page, int booksPerPage) {
        int startIndex = booksPerPage * (page - 1);
//        int amount = startIndex + booksPerPage;
//        logger.info(jdbcTemplate.query("SELECT * FROM announcement WHERE approved = true ORDER BY title LIMIT 5 OFFSET 1", this::mapRowToAnnouncement));
        return jdbcTemplate.query("SELECT * FROM announcement WHERE approved = true ORDER BY announcment_id LIMIT " + booksPerPage + " OFFSET " + startIndex, this::mapRowToAnnouncement);
    }

    @Override
    public List<ViewBook> findViewBooksByTitleOrAuthor(String titleOrAuthor) {
        titleOrAuthor = "%" + titleOrAuthor + "%";
        SqlParameterSource namedParameters = new MapSqlParameterSource("titleOrAuthor", titleOrAuthor);
        return namedParameterJdbcTemplate.query(env.getProperty("findBooksByTitleOrAuthor"), namedParameters, viewBooksMapper);
    }


    @Override
    public List<ViewBook> findViewBooksByFilter(String title, String author, String genre, Date date1, Date date2, int page1, int page2) {
        return null;
    }

    @Override
    public ViewBook getBookById(int id) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
        return (ViewBook) namedParameterJdbcTemplate.query(env.getProperty("getBookById"), namedParameters, viewBooksMapper).get(0);
    }

    @Override
    public List<Book> findAllBooks() {
        return jdbcTemplate.query("select * from book", new BookRowMapper());
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

        return namedParameterJdbcTemplate.query(sql, namedParameters, new BookRowMapper());
    }

    @Override
    public List<Announcement> findAllAnnouncement() {
        return jdbcTemplate.query("SELECT * FROM announcement WHERE approved = true", this::mapRowToAnnouncement);
    }
    @Override
    public String addBook(Book book) {
        jdbcTemplate.update("insert into book (title, likes, image_path, release_date, lang, pages, approved) " + "values(?, ?, ?, TO_DATE(?, 'yyyy-mm-dd'), ?, ?, ?)",
                new Object[] {book.getTitle(), book.getLikes(), book.getImagePath(), book.getReleaseDate(), book.getLang(), book.getPages(), book.isApproved()});
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

}
