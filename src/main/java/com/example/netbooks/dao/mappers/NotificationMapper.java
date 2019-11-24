package com.example.netbooks.dao.mappers;

import com.example.netbooks.models.Notification;
import com.example.netbooks.models.Review;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificationMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Notification(resultSet.getInt("notification_id"),
                resultSet.getInt("user_id"),
                resultSet.getString("notif_name"),
                resultSet.getString("notif_title"),
                resultSet.getString("notif_text"),
                resultSet.getString("notif_date"),
                resultSet.getBoolean("is_read"));

    }
}
