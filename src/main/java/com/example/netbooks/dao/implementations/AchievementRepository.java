package com.example.netbooks.dao.implementations;


import com.example.netbooks.models.Achievement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
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
            achievement.setN(resultSet.getInt("n"));
            achievement.setImagePath(resultSet.getString("image_path"));
            return achievement;
        }
    }
    public List<Achievement> findByAchievementId(Long id) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("user_id", id);
            return namedJdbcTemplate.query(env.getProperty("findByAchievementId"),
                    namedParams, new AchievementMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Achievment not found - " + id);
            return null;
        }
    }
    public Long getAchvIdByDescription(String achvType, int n){
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("achvType", achvType);
        namedParameters.addValue("n", n);
        return namedJdbcTemplate.queryForObject(env.getProperty("getAchvIdByDesc"), namedParameters, Long.class);
    }

    public void addAchievementForUser(long achvId, long userId){
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("achvId", achvId);
        namedParameters.addValue("userId", userId);
        namedJdbcTemplate.update(env.getProperty("addAchievementForUser"), namedParameters);
    }

}
