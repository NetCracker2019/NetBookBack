package com.example.netbooks.dao.implementations;

import com.example.netbooks.dao.interfaces.NotificationRepository;
import com.example.netbooks.dao.mappers.NotificationMapper;
import com.example.netbooks.dao.mappers.ViewNotificationMapper;
import com.example.netbooks.models.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@PropertySource("classpath:queries/notification.properties")
@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

    @Autowired
    Environment environment;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    JdbcTemplate jdbcTemplate;
    private final RowMapper notificationMapper = new NotificationMapper();
    private final RowMapper viewNotificationMapper = new ViewNotificationMapper();

    @Override
    public List<Notification> getAllViewNotificationsByUserId(long userId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("user_id", userId);
        return namedParameterJdbcTemplate.query(environment.getProperty("getAllViewNotificationsByUserId"), namedParameters, viewNotificationMapper);
    }

    @Override
    public List<Notification> getAllNotificationsByUserId(long userId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("user_id", userId);
        return namedParameterJdbcTemplate.query(environment.getProperty("getAllNotificationsByUserId"), namedParameters, notificationMapper);
    }


    @Override
    public void addNotification(Notification notification) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_id", notification.getUserId());
        namedParams.put("from_user_id", notification.getFromUserId());
        namedParams.put("notif_type_id", notification.getNotifTypeId());
        namedParams.put("overview_id", notification.getOverviewId());
        namedParams.put("review_id", notification.getReviewId());
        namedParams.put("notif_date", notification.getDate());
        namedParams.put("is_read", notification.getIsRead());
        namedParams.put("book_id", notification.getBookId());
        namedParams.put("achiev_id", notification.getAchievId());
        namedParameterJdbcTemplate.update(environment.getProperty("addNotification"), namedParams);
    }


    @Override
    public void markAsRead() {
        Map<String, Object> namedParams = new HashMap<>();
        namedParameterJdbcTemplate.update(environment.getProperty("markAsRead"), namedParams);
    }

}

