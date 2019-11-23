package com.example.netbooks.dao.implementations;

import com.example.netbooks.dao.interfaces.ReviewRepository;
import com.example.netbooks.dao.mappers.ReviewMapper;
import com.example.netbooks.models.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;
import java.util.List;

@PropertySource("classpath:queries/review.properties")
@Repository
public class ReviewRepositoryImpl implements ReviewRepository {
    @Autowired
    Environment env;
    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    @Autowired
    JdbcTemplate jdbcTemplate;
    private final RowMapper reviewMapper = new ReviewMapper();

    @Autowired
    public ReviewRepositoryImpl(DataSource dataSource) {
        namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int countReviews(){
        return jdbcTemplate.queryForObject(env.getProperty("countReviews"), Integer.class);
    }

    @Override
    public boolean addReviewForUserBook(Review review) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("bookId", review.getBookId());
        namedParameters.addValue("userId", review.getUserId());
        namedParameters.addValue("reviewText", review.getReviewText());
        return false;
        //return namedJdbcTemplate.update(env.getProperty("addReviewForUserBook"), namedParameters);
    }

    @Override
    public void approveReview(Review review) {

    }

    @Override
    public List<Review> getReviewsByBookId(long bookId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("bookId", bookId);
        return namedJdbcTemplate.query(env.getProperty("getReviewsByBookId"), namedParameters, reviewMapper);
    }
    @Override
    public List<Review> getPeaceOfReviewByBook(int bookId, int page, int offset) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("bookId", bookId);
        namedParameters.addValue("count", page);
        namedParameters.addValue("offset", offset);
        return namedJdbcTemplate.query(env.getProperty("getReviewPeaceByBookId"), namedParameters, reviewMapper);
    }
}
