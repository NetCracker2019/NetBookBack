package com.example.netbooks.dao.mappers;

import com.example.netbooks.models.ViewAnnouncement;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewAnnouncementMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        return new ViewAnnouncement(
                resultSet.getInt("announcmentId"),
                resultSet.getString("releaseDate"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getString("imagePath"),
                (String[])resultSet.getArray("authors").getArray(),
                (String[])resultSet.getArray("genres").getArray());
    }
}
