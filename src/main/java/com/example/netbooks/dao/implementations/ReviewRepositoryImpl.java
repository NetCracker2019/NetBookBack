package com.example.netbooks.dao.implementations;

import com.example.netbooks.dao.interfaces.ReviewRepository;
import com.example.netbooks.dao.mappers.ReviewMapper;
import com.example.netbooks.models.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private final Environment env;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper reviewMapper = new ReviewMapper();
    @Value("${getReviewPeaceForApprove}")
    String getReviewPeaceForApprove;

    @Autowired
    public ReviewRepositoryImpl(DataSource dataSource, Environment env) {
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.env = env;
    }

    @Override
    public int countReviews(boolean approved){
        SqlParameterSource namedParameters = new MapSqlParameterSource("approved", approved);
        return namedJdbcTemplate.queryForObject(env.getProperty("countReviews"), namedParameters, Integer.class);
    }

    @Override
    public boolean addReviewForUserBook(Review review) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("bookId", review.getBookId());
        namedParameters.addValue("userId", review.getUserId());
        namedParameters.addValue("reviewText", review.getReviewText());
        namedParameters.addValue("approved", review.isApproved());
        return namedJdbcTemplate.update(env.getProperty("addReviewForUserBook"), namedParameters) > 0;
    }

    @Override
    public boolean approveReview(long reviewId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("reviewId", reviewId);
        return namedJdbcTemplate.update(env.getProperty("approveReview"), namedParameters) > 0;
    }

    @Override
    public boolean cancelReview(long reviewId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("reviewId", reviewId);
        return namedJdbcTemplate.update(env.getProperty("cancelReview"), namedParameters) > 0;
    }

    @Override
    public List<Review> getReviewsForApprove(int page, int itemPerPage) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("count", itemPerPage);
        namedParameters.addValue("offset", page);
        System.out.println(getReviewPeaceForApprove);
        return namedJdbcTemplate.query(getReviewPeaceForApprove, namedParameters, reviewMapper);
    }

    @Override
    public int countReviewsForUser(long userId) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("userId", userId);
        return namedJdbcTemplate.queryForObject(env.getProperty("countUserReviews"), namedParameters, Integer.class);
    }

    @Override
    public void likeReview(long reviewId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("reviewId", reviewId);
        namedJdbcTemplate.update(env.getProperty("likeReview"), namedParameters);
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
