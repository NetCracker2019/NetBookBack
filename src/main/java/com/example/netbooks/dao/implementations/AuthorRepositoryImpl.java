package com.example.netbooks.dao.implementations;

import com.example.netbooks.dao.interfaces.AuthorRepository;
import com.example.netbooks.dao.mappers.AuthorMapper;
import com.example.netbooks.models.Author;
import com.example.netbooks.models.Book;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {
    private JdbcTemplate jdbcTemplate;
    private AuthorMapper authorMapper = new AuthorMapper();
    @Autowired
    Environment env;
    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;


    public AuthorRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Author> getAllAuthors() {
        return jdbcTemplate.query("SELECT * FROM author", authorMapper);
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
