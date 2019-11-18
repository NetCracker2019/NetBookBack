package com.example.netbooks.dao;

import com.example.netbooks.controllers.AuthenticationController;
import com.example.netbooks.models.Announcement;
import com.example.netbooks.models.Book;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcBookRepository implements BookRepository {
    private final Logger logger = LogManager.getLogger(AuthenticationController.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public int getAmountOfAnnouncement() {
        return jdbcTemplate.queryForObject("SELECT COUNT (*) FROM announcement;", Integer.class);
    }

    @Override
    public List<Announcement> findAllAnnouncement() {
        return jdbcTemplate.query("SELECT * FROM announcement WHERE approved = true", this::mapRowToAnnouncement);
    }

    @Override
    public List<Announcement> getPeaceAnnouncement(int page, int booksPerPage) {
        int startIndex = booksPerPage * (page - 1);
//        int amount = startIndex + booksPerPage;
//        logger.info(jdbcTemplate.query("SELECT * FROM announcement WHERE approved = true ORDER BY title LIMIT 5 OFFSET 1", this::mapRowToAnnouncement));
        return jdbcTemplate.query("SELECT * FROM announcement WHERE approved = true ORDER BY announcment_id LIMIT " + booksPerPage + " OFFSET " + startIndex, this::mapRowToAnnouncement);
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
}