package com.example.netbooks.dao.mappers;

import com.example.netbooks.models.Notification;
import com.example.netbooks.models.Review;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewNotificationMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Notification(
                resultSet.getDate("notif_date"),
                resultSet.getInt("notif_type_id"),
                resultSet.getString("overview_name"),
                resultSet.getString("review_name"),
                resultSet.getString("from_user_name"),
                resultSet.getString("book_name"),
                resultSet.getString("achiev_name"),
                resultSet.getString("notif_title"),
                resultSet.getString("notif_text")
        );
    }
}
