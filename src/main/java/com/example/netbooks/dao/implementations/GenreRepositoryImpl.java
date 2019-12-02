package com.example.netbooks.dao.implementations;

import com.example.netbooks.controllers.ProfileController;
import com.example.netbooks.dao.interfaces.GenreRepository;
import com.example.netbooks.dao.mappers.GenreMapper;
import com.example.netbooks.models.Book;
import com.example.netbooks.models.Genre;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GenreRepositoryImpl implements GenreRepository {
    //@Autowired
    JdbcTemplate jdbcTemplate;
    private final Logger logger = LogManager.getLogger(GenreRepositoryImpl.class);
    public GenreRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    @Override
    public List<String> getAllGenreNames() {
        return jdbcTemplate.queryForList("SELECT genre_name FROM genre", String.class);
    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("SELECT * FROM genre", new GenreMapper());
    }

    @Override
    public String addRowIntoBookGenre(String title, List<String> id) {
        logger.info("Я добавил жанр");
        for (String item : id) {
            //for (int i = 0; i < book.getGenre().size(); i++) {
            //todo prepared statement
                jdbcTemplate.update("insert into book_genre values ((select book_id from book where title ='" + title
                        + "'), " + item + ")");
            //}
        }
            return "Add Genre";

    }

}


