package com.example.netbooks.dao;

import com.example.netbooks.models.Announcement;
import com.example.netbooks.models.Book;
import com.example.netbooks.models.ShortBookDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@PropertySource("classpath:queries/book.properties")
public class JdbcBookRepository implements BookRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    @Autowired
    private Environment env;


    @Override
    public List<Announcement> findAllAnnouncement() {
        return jdbcTemplate.query("SELECT * FROM announcement WHERE approved = true", this::mapRowToAnnouncement);
    }

    @Override
    public List<Book> findAll() {
        return jdbcTemplate.query("SELECT * FROM book", this::mapRowToBook);
    }


    @Override
    public String addBook(Book book) {
        jdbcTemplate.update("insert into book (title, likes, image_path, release_date, lang, pages, approved) " + "values(?, ?, ?, TO_DATE(?, 'yyyy-mm-dd'), ?, ?, ?)",
                new Object[] {book.getTitle(), book.getLike(), book.getImagePath(), book.getRelease_date(), book.getLanguage(), book.getPages(), book.isApproved()});
        return "Complete!";
    }


    private Book mapRowToBook(ResultSet resultSet, int i) throws SQLException {
        return new Book(
                resultSet.getLong("book_id"),
                resultSet.getString("title"),
                resultSet.getInt("likes"),
                resultSet.getString("image_path"),
                resultSet.getString("release_date"),
                resultSet.getString("lang"),
                resultSet.getInt("pages"),
                resultSet.getBoolean("approved"));
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