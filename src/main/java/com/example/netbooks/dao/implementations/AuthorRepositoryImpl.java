package com.example.netbooks.dao.implementations;

import com.example.netbooks.dao.interfaces.AuthorRepository;
import com.example.netbooks.dao.mappers.AuthorMapper;
import com.example.netbooks.models.Author;
import com.example.netbooks.models.Book;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {
    private JdbcTemplate jdbcTemplate;
    private AuthorMapper authorMapper = new AuthorMapper();


    public AuthorRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Author> getAllAuthors() {
        return jdbcTemplate.query("SELECT * FROM author", authorMapper);
    }



    @Override
    public String addRowIntoBookAuthor(String title, List<String> id) {
        for (String item : id) {
            boolean isThisAuthorExist = jdbcTemplate.queryForObject("select exists(select 1 from author where fullname='" + item + "')", Boolean.class);
            if (isThisAuthorExist == false) {
                jdbcTemplate.update("insert into author (fullname) values (?)", new Object[] {item});
            }
            jdbcTemplate.update("insert into book_author values ((select book_id from book where title ='" + title + "'), \n" +
                    "\t\t   (select author_id from author where fullname ='" +  item + "'))");
        }
        return "Add Author";
    }
}
