package com.example.netbooks.dao.mappers;

import com.example.netbooks.models.Author;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorNameMapper implements RowMapper<Author> {
    @Override
    public Author mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Author(
                resultSet.getString("authors")
        );
    }
}