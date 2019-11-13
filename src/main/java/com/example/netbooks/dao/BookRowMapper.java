package com.example.netbooks.dao;

import com.example.netbooks.models.Book;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookRowMapper implements RowMapper<Book> {
    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Book(
                resultSet.getLong("book_id"),
                resultSet.getString("title"),
                resultSet.getLong("likes"),
                resultSet.getString("image_path"),
                resultSet.getDate("release_date"),
                resultSet.getString("lang"),
                resultSet.getInt("pages"),
                resultSet.getBoolean("approved"));
    }
}