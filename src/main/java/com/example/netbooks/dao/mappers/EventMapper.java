package com.example.netbooks.dao.mappers;

import com.example.netbooks.models.Event;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Event(
                resultSet.getString("title"),
                resultSet.getString("date"),
                resultSet.getInt("id"));
    }
}
