package com.example.netbooks.dao;

import com.example.netbooks.controllers.AuthenticationController;
import com.example.netbooks.models.User;
import com.example.netbooks.models.VerificationToken;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@PropertySource("classpath:queries/token.properties")
public class VerificationTokenRepository {

	private final Logger logger = LogManager.getLogger(AuthenticationController.class);
	final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment env;
    @Autowired
    public VerificationTokenRepository(NamedParameterJdbcTemplate namedJdbcTemplate, Environment env) {
		super();
		this.namedJdbcTemplate = namedJdbcTemplate;
		this.env = env;
	}
    
    private final class TokenMapper implements RowMapper<VerificationToken> {
        @Override
        public VerificationToken mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        	VerificationToken token = new VerificationToken();        
        	token.setCreatedDate(resultSet.getDate("created_date"));
        	token.setTokenId(resultSet.getLong("token_id"));
        	token.setUserId (resultSet.getInt("user_id"));
        	token.setVerificationToken(resultSet.getString("token_name"));
        	return token;     
        }
    }
    
    public void save(VerificationToken token) {
    	Map<String, Object> namedParams = new HashMap<>();
    	namedParams.put("created_date", token.getCreatedDate());
    	//namedParams.put("token_id", token.getTokenId());
    	namedParams.put("user_id", token.getUserId());
    	namedParams.put("token_name", token.getVerificationToken());
        
        namedJdbcTemplate.update(env.getProperty("saveToken"), namedParams);
    }
    
    public void removeVerificationToken(String token) {
    	Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("token_name", token);
        namedJdbcTemplate.update(env.getProperty("deleteToken"), namedParams);
    }

    
    public VerificationToken findByVerificationToken(String token) {
    	try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("token_name", token);
            return namedJdbcTemplate.queryForObject(env.getProperty("findByVerificationToken"), namedParams, new TokenMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("Token not found - " + token);
            return null;
        }
    }
}