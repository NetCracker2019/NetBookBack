package com.example.netbooks.dao.mappers;

import com.example.netbooks.models.Genre;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreNameMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Genre genre = new Genre (
                resultSet.getString("genres"));
        return genre;
    }
}
