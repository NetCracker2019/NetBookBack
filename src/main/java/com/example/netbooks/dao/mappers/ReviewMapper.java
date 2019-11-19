package com.example.netbooks.dao.mappers;

import com.example.netbooks.models.Review;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Review(
                resultSet.getLong("r_id"),
                resultSet.getLong("u_id"),
                resultSet.getLong("b_id"),
                resultSet.getString("u_name"),
                resultSet.getString("av_path"),
                resultSet.getString("r_text"),
                resultSet.getInt("rating"),
                resultSet.getBoolean("appr"));
    }
}