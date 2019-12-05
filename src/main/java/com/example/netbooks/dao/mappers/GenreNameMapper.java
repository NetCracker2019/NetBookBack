package com.example.netbooks.dao.mappers;

import com.example.netbooks.models.Genre;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreNameMapper implements RowMapper<Genre> {

    @Override
    public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Genre (
                resultSet.getString("genres"));
    }
}
