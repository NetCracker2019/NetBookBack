package com.example.netbooks.dao.mappers;

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
                resultSet.getString("author"),
                resultSet.getString("genre"),
                resultSet.getInt("likes"),
                resultSet.getString("image_path"),
                resultSet.getString("release_date"),
                resultSet.getString("lang"),
                resultSet.getInt("pages"),
                resultSet.getString("description"),
                resultSet.getBoolean("approved"));
    }
}