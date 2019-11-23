package com.example.netbooks.dao.implementations;

import com.example.netbooks.dao.interfaces.AuthorRepository;
import com.example.netbooks.dao.mappers.AuthorMapper;
import com.example.netbooks.models.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {
    private JdbcTemplate jdbcTemplate;
    private AuthorMapper authorMapper = new AuthorMapper();

    @Autowired
    public AuthorRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Author> getAllAuthors() {
        return jdbcTemplate.query("SELECT * FROM author", authorMapper);
    }
}
