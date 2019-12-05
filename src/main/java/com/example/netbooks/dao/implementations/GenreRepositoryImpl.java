package com.example.netbooks.dao.implementations;

import com.example.netbooks.dao.interfaces.GenreRepository;
import com.example.netbooks.models.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@PropertySource("classpath:queries/genre.properties")
@Repository
public class GenreRepositoryImpl implements GenreRepository {
    private final Environment env;
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Genre> genreMapper;

    @Autowired
    public GenreRepositoryImpl(DataSource dataSource,
                               RowMapper<Genre> genreMapper,
                               Environment env) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.genreMapper = genreMapper;
        this.env = env;
    }

    @Override
    public List<String> getAllGenreNames() {
        return jdbcTemplate.queryForList(env.getRequiredProperty("getAllGenresNames"), String.class);
    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query(env.getRequiredProperty("getAllGenres"), genreMapper);
    }

    @Override
    public String addRowIntoBookGenre(String title, List<String> id) {
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


