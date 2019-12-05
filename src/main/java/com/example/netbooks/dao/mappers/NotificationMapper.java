package com.example.netbooks.dao.mappers;

import com.example.netbooks.models.Notification;
import com.example.netbooks.models.Review;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class NotificationMapper implements RowMapper<Notification> {
    @Override
    public Notification mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Notification(resultSet.getInt("notification_id"),
                resultSet.getInt("user_id"),
                resultSet.getString("notif_name"),
                resultSet.getString("notif_title"),
                resultSet.getString("notif_text"),
                resultSet.getDate("notif_date"),
                resultSet.getBoolean("is_read"));

    }
}
