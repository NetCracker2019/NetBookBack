package com.example.netbooks.dao;

import com.example.netbooks.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcBookRepository implements BookRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Book> findAll() {
        return jdbcTemplate.query("SELECT * FROM book", new BookRowMapper());
    }

    @Override
    public List<Book> findBooksByTitle(String title) {
        title = "%" + title + "%";
        return jdbcTemplate.query("SELECT * FROM book WHERE lower(title) LIKE ?",
                new Object[] {title}, new BookRowMapper());
    }
}