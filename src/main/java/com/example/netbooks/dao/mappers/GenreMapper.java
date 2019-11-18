package com.example.netbooks.dao.mappers;

import com.example.netbooks.models.Genre;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Genre genre = new Genre(
                resultSet.getLong("genre_id"),
                resultSet.getString("genre_name"));

        return genre;
    }
}