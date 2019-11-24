package com.example.netbooks.dao.implementations;

import com.example.netbooks.controllers.AuthenticationController;
import com.example.netbooks.models.Book;
import com.example.netbooks.models.Role;
import com.example.netbooks.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@PropertySource("classpath:queries/user.properties")
@Repository
public class UserRepository {
	private final Logger logger = LogManager.getLogger(AuthenticationController.class);
	private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final Environment env;

    private final class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            User user = new User();
            
            user.setUserId(resultSet.getLong("person_id"));
            user.setLogin(resultSet.getString("login"));
            user.setPassword(resultSet.getString("passw"));
            user.setName(resultSet.getString("person_name"));
            user.setEmail(resultSet.getString("mail"));
            user.setAvatarFilePath(resultSet.getString("avatar_filepath"));
            user.setSex(resultSet.getString("sex"));
            user.setCountry(resultSet.getString("country"));
            user.setCity(resultSet.getString("city"));
            user.setStatus(resultSet.getString("description"));
            user.setActivity(resultSet.getBoolean("activity"));
            user.setTurnOnNotif(resultSet.getBoolean("turn_on_notif"));
            user.setRegDate(resultSet.getDate("reg_date"));
            user.setRoleInt(resultSet.getInt("role_id"));
            user.setMinRefreshDate(resultSet.getDate("min_refresh_date"));
            return user;
        }
    }
    private final class FriendMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            User user = new User();
            user.setLogin(resultSet.getString("login"));
            user.setName(resultSet.getString("person_name"));
            user.setAvatarFilePath(resultSet.getString("avatar_filepath"));
            return user;
        }
    }

    
    @Autowired
    public UserRepository(NamedParameterJdbcTemplate namedJdbcTemplate, Environment env) {
		super();
		this.namedJdbcTemplate = namedJdbcTemplate;
		this.env = env;
	}
   
    public void save(User user) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("login", user.getLogin());
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
        namedParams.put("min_refresh_date",  user.getMinRefreshDate());

        namedJdbcTemplate.update(env.getProperty("saveUser"), namedParams);
    }
    
    public void updateUser(User user) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("login", user.getLogin());
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
        namedParams.put("min_refresh_date",  user.getMinRefreshDate());

        namedJdbcTemplate.update(env.getProperty("updateUser"), namedParams);
    }
    
    public void updateUserById(User user, Long id) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("login", user.getLogin());
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
        namedParams.put("min_refresh_date",  user.getMinRefreshDate());
        namedParams.put("person_id", id);

        namedJdbcTemplate.update(env.getProperty("updateUserById"), namedParams);
    }
    
    public Iterable<User> getAllUsers() {
        return namedJdbcTemplate.query(env.getProperty("getAllUsers"), new UserMapper());
    }
    
    public User findByEmail(String email) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("mail", email);
            return namedJdbcTemplate.queryForObject(env.getProperty("findUserByEmail"), namedParams, new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("User not found - " + email);
            return null;
        }
    }
    
    public User findByLogin(String login) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("login", login);
            return namedJdbcTemplate.queryForObject(env.getProperty("findByLogin"), namedParams, new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("User not found - " + login);
            return null;
        }
    }

    public User findByUserId(Long id) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("person_id", id);
            return namedJdbcTemplate.queryForObject(env.getProperty("findByUserId"), namedParams, new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("User not found - " + id);
            return null;
        }
    }
    
    public void removeUserById(Long id) {
    	Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("person_id", id);
        namedJdbcTemplate.update(env.getProperty("removeUserById"), namedParams);
    }
    
    public void activateUser(Long id) {
    	Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("person_id", id);
        namedJdbcTemplate.update(env.getProperty("activateUser"), namedParams);
    }
    
    public void deActivateUser(Long id) {
    	Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("person_id", id);
        namedJdbcTemplate.update(env.getProperty("deActivateUser"), namedParams);
    }

	public void setMinRefreshDate(String login, Date date) {
		Map<String, Object> namedParams = new HashMap<>();
		namedParams.put("min_refresh_date", date);
		namedParams.put("login", login);
        namedJdbcTemplate.update(env.getProperty("setMinRefreshDate"), namedParams);
	}

    public List<User> getFriendsByLogin(String login, int cntFriends, int offset) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("id", findByLogin(login).getUserId());
            namedParams.put("offset", offset);
            namedParams.put("cnt", cntFriends);
            return namedJdbcTemplate.query(env.getProperty("getFriendsByLogin"), namedParams, new FriendMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public List<User> getPersonsBySought(String sought, int cntPersons, int offset) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("sought", "%" + sought + "%");
            namedParams.put("offset", offset);
            namedParams.put("cnt", cntPersons);
            return namedJdbcTemplate.query(env.getProperty("getPersonsBySought"), namedParams, new FriendMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public List<User> getFriendsBySought(String login, String sought, int cntPersons, int offset) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("id", findByLogin(login).getUserId());
            namedParams.put("sought", "%" + sought + "%");
            namedParams.put("offset", offset);
            namedParams.put("cnt", cntPersons);
            return namedJdbcTemplate.query(env.getProperty("getFriendsBySought"), namedParams, new FriendMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public String getUserRole (String login) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("login", login);
        return namedJdbcTemplate.queryForObject(env.getProperty("getUserRole"), namedParams, String.class);
    }
}

