package com.example.netbooks.dao;


import com.example.netbooks.models.Achievement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@PropertySource("classpath:queries/achievement.properties")
@Repository
public class AchievementRepository {
    private final Logger logger = LogManager.getLogger(AchievementRepository.class);
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment env;
    @Autowired
    public AchievementRepository(NamedParameterJdbcTemplate namedJdbcTemplate, Environment env) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.env = env;
    }
    private final class AchievementMapper implements RowMapper<Achievement> {
        @Override
        public Achievement mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Achievement achievement = new Achievement();

            achievement.setTitle(resultSet.getString("title"));
            achievement.setDescription(resultSet.getString("description"));
            achievement.setCntBook(resultSet.getInt("n_books"));
            achievement.setImagePath(resultSet.getString("image_path"));
            return achievement;
        }
    }
    public Achievement findByAchievementId(Long id) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_id", id);
            return namedJdbcTemplate.queryForObject(env.getProperty("findByAchievementId"),
                    namedParams, new AchievementMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Achievment not found - " + id);
            return null;
        }
    }


}
