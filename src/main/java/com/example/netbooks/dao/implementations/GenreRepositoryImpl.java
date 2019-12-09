package com.example.netbooks.dao.implementations;

import com.example.netbooks.controllers.ProfileController;
import com.example.netbooks.dao.interfaces.GenreRepository;
import com.example.netbooks.dao.mappers.GenreMapper;
import com.example.netbooks.models.Book;
import com.example.netbooks.models.Genre;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GenreRepositoryImpl implements GenreRepository {
    //@Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    Environment env;
    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;
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
    public String addRowIntoBookGenre(String title, String description, List<String> id) {



        for (String item : id) {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("title", title);
            namedParameters.addValue("description", description);
            namedParameters.addValue("item", item);
            //for (int i = 0; i < book.getGenre().size(); i++) {
            //todo prepared statement
            logger.info(namedParameters);
            namedJdbcTemplate.update(env.getProperty("addRowIntoBookGenre"), namedParameters);
//                jdbcTemplate.update("insert into book_genre values ((select book_id from book where title ='" + title
//                        + "'), " + item + ")");
            //}
        }
            return "Add Genre";

    }

}


