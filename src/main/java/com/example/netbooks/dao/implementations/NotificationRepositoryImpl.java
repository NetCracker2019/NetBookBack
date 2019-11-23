package com.example.netbooks.dao.implementations;

import com.example.netbooks.dao.interfaces.NotificationRepository;
import com.example.netbooks.dao.mappers.NotificationMapper;
import com.example.netbooks.models.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

    @Autowired
    Environment environment;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    RowMapper notificationMapper = new NotificationMapper();

    @Override
    public List<Notification> getAllNotificationsByUserId(long userId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("user_id", userId);
        return namedParameterJdbcTemplate.query(environment.getProperty("getAllNotificationsByUserId"), namedParameters, notificationMapper);
    }

    //TODO dopisat vse metody
    @Override
    public void addNotification(Notification notification) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_id", user.getLogin());
        namedParams.put("passw", user.getPassword());
        namedParams.put("person_name", user.getName());
        namedParams.put("mail", user.getEmail());
        namedParams.put("avatar_filepath", user.getAvatarFilePath());
        namedParams.put("sex", user.getSex());
        namedParams.put("country", user.getCountry());
        namedParams.put("city", user.getCity());
        namedParams.put("description", user.getStatus());
        namedParams.put("activity", user.isActivity());
        namedParams.put("turn_on_notif", user.isTurnOnNotif());
        namedParams.put("reg_date", user.getRegDate());
        namedParams.put("role_id", user.getRole().ordinal() + 1);
        namedParams.put("min_refresh_date", user.getMinRefreshDate());
        /*resultSet.getInt("notification_id"),
                resultSet.getInt("user_id"),
                resultSet.getString("notif_name"),
                resultSet.getString("notif_title"),
                resultSet.getString("notif_text"),
                resultSet.getDate("date"),
                resultSet.getTime("time"),
                resultSet.getBoolean("is_read"));*/

        namedJdbcTemplate.update(env.getProperty("saveUser"), namedParams)
    }
//todo sho ce za dych????
    @Override
    public void markAsRead() {
        Map<String, Object> namedParams = new HashMap<>();
        namedParameterJdbcTemplate.update(environment.getProperty("markAsRead"),namedParams);
    }

    @Override
    public Notification getNotification(int notificationId) {

        SqlParameterSource namedParameters = new MapSqlParameterSource("notification_id", notificationId);
        return  namedParameterJdbcTemplate.queryForRowSet(environment.getProperty("getNotificationById"),namedParameters,notificationMapper);
        /*
        return namedParameterJdbcTemplate.query(environment.getProperty("getNotificationById"), namedParameters, notificationMapper);
          */
    }
}