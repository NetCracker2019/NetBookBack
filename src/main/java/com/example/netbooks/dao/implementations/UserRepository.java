package com.example.netbooks.dao.implementations;

import com.example.netbooks.controllers.AuthenticationController;
import com.example.netbooks.dao.mappers.FriendMapper;
import com.example.netbooks.exceptions.CustomException;
import com.example.netbooks.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@PropertySource("classpath:queries/user.properties")
@Repository
public class UserRepository implements com.example.netbooks.dao.interfaces.UserRepository {
	private final Logger logger = LogManager.getLogger(AuthenticationController.class);
	private final NamedParameterJdbcTemplate namedJdbcTemplate;

    private static final class UserMapper implements RowMapper<User> {
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

    @Autowired
    public UserRepository(DataSource dataSource) {
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Value("${saveUser}")
    private String saveUser;

    @Value("${updateUser}")
    private String updateUser;

    @Value("${updateUserById}")
    private String updateUserById;

    @Value("${getAllUsers}")
    private String getAllUsers;

    @Value("${getUserIdByLogin}")
    private String getUserIdByLogin;

    @Value("${countFriendsForUser}")
    private String countFriendsForUser;

    @Value("${findUserByEmail}")
    private String findUserByEmail;

    @Value("${findByLogin}")
    private String findByLogin;

    @Value("${findByUserId}")
    private String findByUserId;

    @Value("${removeUserById}")
    private String removeUserById;

    @Value("${activateUser}")
    private String activateUser;

    @Value("${deActivateUser}")
    private String deActivateUser;

    @Value("${setMinRefreshDate}")
    private String setMinRefreshDate;

    @Value("${getPersonsBySought}")
    private String getPersonsBySought;

    @Value("${getFriendsByLogin}")
    private String getFriendsByLogin;

    @Value("${getClientPersonsBySought}")
    private String getClientPersonsBySought;

    @Value("${getFriendsBySought}")
    private String getFriendsBySought;

    @Value("${getUserRole}")
    private String getUserRole;

    @Value("${getCountFriendsBySought}")
    private String getCountFriendsBySought;

    @Value("${getCountPersonsBySought}")
    private String getCountPersonsBySought;

    @Value("${isSubscribe}")
    private String isSubscribe;

    @Value("${acceptFriendRequest}")
    private String acceptFriendRequest;

    @Value("${addFriend}")
    private String addFriend;

    @Value("${isFriend}")
    private String isFriend;

    @Value("${deleteFriend}")
    private String deleteFriend;

    @Value("${getSubscribersByLogin}")
    private String getSubscribersByLogin;

    @Value("${getFriendsByUsername}")
    private String getFriendsByUsername;

    @Override
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

        namedJdbcTemplate.update(saveUser, namedParams);
    }
    @Override
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

        namedJdbcTemplate.update(updateUser, namedParams);
    }
    @Override
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

        namedJdbcTemplate.update(updateUserById, namedParams);
    }
    @Override
    public Iterable<User> getAllUsers() {
        return namedJdbcTemplate.query(getAllUsers, new UserMapper());
    }
    @Override
    public Integer getUserIdByLogin(String login){
        SqlParameterSource namedParameters = new MapSqlParameterSource("login", login);
        return namedJdbcTemplate.queryForObject(getUserIdByLogin, namedParameters, Integer.class);
    }
    @Override
    public Integer countFriendsForUser(long userId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("userId", userId);
        return namedJdbcTemplate.queryForObject(countFriendsForUser, namedParameters, Integer.class);

    }
    @Override
    public User findByEmail(String email) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("mail", email);
            return namedJdbcTemplate.queryForObject(findUserByEmail, namedParams, new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("User not found - " + email);
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);
        }
    }
    @Override
    public User findByLogin(String login) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("login", login);
            return namedJdbcTemplate.queryForObject(findByLogin, namedParams, new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("User not found - " + login);
            throw new CustomException("User not found ", HttpStatus.NOT_FOUND);
        }
    }
    @Override
    public User findByUserId(Long id) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("person_id", id);
            return namedJdbcTemplate.queryForObject(findByUserId, namedParams, new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            logger.info("User not found - " + id);
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);
        }
    }
    @Override
    public Boolean isExistByLogin(String login) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("login", login);
            namedJdbcTemplate.queryForObject(findByLogin, namedParams, new UserMapper());
            return true;
        } catch (EmptyResultDataAccessException e) {
            logger.info("User not found by login - " + login);
            return false;
        }
    }
    @Override
    public Boolean isExistByMail(String mail) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("mail", mail);
            namedJdbcTemplate.queryForObject(findUserByEmail, namedParams, new UserMapper());
            return true;
        } catch (EmptyResultDataAccessException e) {
            logger.info("User not found by mail - " + mail);
            return false;
        }
    }
    @Override
    public void removeUserById(Long id) {
    	Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("person_id", id);
        namedJdbcTemplate.update(removeUserById, namedParams);
    }
    @Override
    public void activateUser(Long id) {
    	Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("person_id", id);
        namedJdbcTemplate.update(activateUser, namedParams);
    }
    @Override
    public void deActivateUser(Long id) {
    	Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("person_id", id);
        namedJdbcTemplate.update(deActivateUser, namedParams);
    }
    @Override
	public void setMinRefreshDate(String login, Date date) {
		Map<String, Object> namedParams = new HashMap<>();
		namedParams.put("min_refresh_date", date);
		namedParams.put("login", login);
        namedJdbcTemplate.update(setMinRefreshDate, namedParams);
	}
    @Override
    public List<User> getFriendsByLogin(String login, int cntFriends, int offset) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("id", findByLogin(login).getUserId());
            namedParams.put("offset", offset);
            namedParams.put("cnt", cntFriends);
            return namedJdbcTemplate.query(getFriendsByLogin, namedParams, new FriendMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("Friends not found", HttpStatus.NOT_FOUND);
        }
    }
    public List<User> getFriendsByUsername(String login) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("id", findByLogin(login).getUserId());
            return namedJdbcTemplate.query(getFriendsByUsername, namedParams, new FriendMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("Friends not found", HttpStatus.NOT_FOUND);
        }
    }

    public List<User> getSubscribersByLogin(String login) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("id", findByLogin(login).getUserId());
            return namedJdbcTemplate.query(getSubscribersByLogin, namedParams, new FriendMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("Friends not found", HttpStatus.NOT_FOUND);
        }
    }


    public List<User> getPersonsBySought(String sought, int cntPersons, int offset) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("sought", "%" + sought + "%");
            namedParams.put("offset", offset);
            namedParams.put("cnt", cntPersons);
            return namedJdbcTemplate.query(getPersonsBySought, namedParams, new FriendMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("Sought not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<User> getClientPersonsBySought(String sought, int cntPersons, int offset) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("sought", "%" + sought + "%");
            namedParams.put("offset", offset);
            namedParams.put("cnt", cntPersons);
            return namedJdbcTemplate.query(getClientPersonsBySought, namedParams, new FriendMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("Sought not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<User> getFriendsBySought(String login, String sought, int cntPersons, int offset) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("id", findByLogin(login).getUserId());
            namedParams.put("sought", "%" + sought + "%");
            namedParams.put("offset", offset);
            namedParams.put("cnt", cntPersons);
            return namedJdbcTemplate.query(getFriendsBySought, namedParams, new FriendMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("Sought not found", HttpStatus.NOT_FOUND);
        }
    }
    @Override
    public String getUserRole(String login) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("login", login);
        return namedJdbcTemplate.queryForObject(getUserRole, namedParams, String.class);
    }
    @Override
    public Integer getCountFriendsBySought(String login, String sought) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("id", findByLogin(login).getUserId());
            namedParams.put("sought", "%" + sought + "%");
            return namedJdbcTemplate.queryForObject(getCountFriendsBySought, namedParams, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("Sought not found", HttpStatus.NOT_FOUND);
        }
    }
    @Override
    public Integer getCountPersonsBySought(String sought) {
        try {
            Map<String, Object> namedParams = new HashMap<>();
            namedParams.put("sought", "%" + sought + "%");
            return namedJdbcTemplate.queryForObject(
                    getCountPersonsBySought, namedParams, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("Sought not found", HttpStatus.NOT_FOUND);
        }
    }
    @Override
    public void addFriend(String ownLogin, String friendLogin) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("ownId", findByLogin(ownLogin).getUserId());
        namedParams.put("friendId", findByLogin(friendLogin).getUserId());
        if(namedJdbcTemplate.queryForObject(
                isSubscribe, namedParams, Integer.class) > 0) {
            namedJdbcTemplate.update(acceptFriendRequest, namedParams);
        }else
        namedJdbcTemplate.update(addFriend, namedParams);
    }
    @Override
    public int isFriend(String ownLogin, String friendLogin) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("ownId", findByLogin(ownLogin).getUserId());
        namedParams.put("friendId", findByLogin(friendLogin).getUserId());
        if(namedJdbcTemplate.queryForObject(
                isFriend, namedParams, Integer.class) > 0){
            return 1;
        }else if(namedJdbcTemplate.queryForObject(
                isSubscribe, namedParams, Integer.class) > 0){
            return 0;
        } else return -1;

    }
    @Override
    public void deleteFriend(String ownLogin, String friendLogin) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("ownId", findByLogin(ownLogin).getUserId());
        namedParams.put("friendId", findByLogin(friendLogin).getUserId());
        namedJdbcTemplate.update(deleteFriend, namedParams);
    }
}

