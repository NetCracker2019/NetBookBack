package com.example.netbooks.dao;

import com.example.netbooks.models.Book;
import com.example.netbooks.models.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GenreRepositoryImpl implements GenreRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public List<String> getAllGenreNames() {
        return jdbcTemplate.queryForList("SELECT genre_name FROM genre", String.class);
    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("SELECT * FROM genre", new GenreRowMapper());
    }
}

class GenreRowMapper implements RowMapper{

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
         Genre genre = new Genre(
                resultSet.getLong("genre_id"),
                resultSet.getString("genre_name"));

        return genre;
    }
}
