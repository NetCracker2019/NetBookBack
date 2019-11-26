package com.example.netbooks.dao.mappers;

import com.example.netbooks.models.ViewBook;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShortViewBookMapper implements RowMapper<ViewBook> {
    @Override
    public ViewBook mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        ViewBook book = new ViewBook();
        book.setImagePath(resultSet.getString("image_path"));
        book.setTitle(resultSet.getString("title"));
        book.setBookId(resultSet.getLong("book_id"));
        book.setFavourite(resultSet.getBoolean("favourite"));
        book.setReading(resultSet.getBoolean("reading"));
        Array tmpArray = resultSet.getArray("authors");
        book.setAuthors((String[])tmpArray.getArray());
        return book;
    }
}