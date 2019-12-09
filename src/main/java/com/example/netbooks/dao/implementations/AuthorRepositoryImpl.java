package com.example.netbooks.dao.implementations;

import com.example.netbooks.dao.interfaces.AuthorRepository;
import com.example.netbooks.models.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@PropertySource("classpath:queries/author.properties")
@Repository
public class AuthorRepositoryImpl implements AuthorRepository {
    private final Environment env;
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Author> authorMapper;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    public AuthorRepositoryImpl(DataSource dataSource,
                                RowMapper<Author> authorMapper,
                                Environment env,
                                NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.authorMapper = authorMapper;
        this.env = env;
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public List<Author> getAllAuthors() {
        return jdbcTemplate.query(env.getRequiredProperty("getAllAuthors") , authorMapper);
    }

    @Override
    public String addRowIntoBookAuthor(String title, String description, List<String> id) {



        for (String item : id) {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("title", title);
            namedParameters.addValue("description", description);
            namedParameters.addValue("fullname", item);

            //boolean isThisAuthorExist = jdbcTemplate.queryForObject("select exists(select 1 from author where fullname='" + item + "')", Boolean.class);
            boolean isThisAuthorExist = namedJdbcTemplate.queryForObject(env.getProperty("isThisAuthorExist"), namedParameters, Boolean.class);
            if (isThisAuthorExist == false) {
                //jdbcTemplate.update("insert into author (fullname) values (?)", new Object[] {item});
                namedJdbcTemplate.update(env.getProperty("addAuthor"), namedParameters);
            }

            namedJdbcTemplate.update(env.getProperty("addRowIntoBookAuthor"), namedParameters);
            //jdbcTemplate.update("insert into book_author values ((select book_id from book where title ='" + title + "'), \n" +
            //        "\t\t   (select author_id from author where fullname ='" +  item + "'))");
        }
        return "Add Author";
    }
}
