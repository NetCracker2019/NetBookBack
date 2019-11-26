package com.example.netbooks.dao.implementations;

import com.example.netbooks.dao.interfaces.ChatRepository;
import com.example.netbooks.dao.mappers.FriendMapper;
import com.example.netbooks.models.Message;
import com.example.netbooks.models.User;
import com.example.netbooks.services.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:queries/chat.properties")
@Repository
public class ChatRepositoryImpl implements ChatRepository {
    Environment env;
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    private UserManager userManager;
    @Autowired
    public ChatRepositoryImpl(Environment env,
                              NamedParameterJdbcTemplate namedJdbcTemplate,
                              UserManager userManager) {
        this.env = env;
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.userManager = userManager;
    }


    @Override
    public List<String> getChatsByUserId(Long userId){
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_id", userId);
        return namedJdbcTemplate.queryForList(env.getProperty("getChatsByUserId"),
                namedParams, String.class);
    }
    @Override
    public void createNewChat(String chatName, List<String> members) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("chat_name", chatName);
        namedJdbcTemplate.update(env.getProperty("createNewChat"), namedParams);
        addMembersToChat(chatName, members);
    }

    @Override
    public void addMembersToChat(String chatName, List<String> members) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("chat_name", chatName);
        namedParams.put("members", members);
        namedJdbcTemplate.update(env.getProperty("addMembersToChat"), namedParams);
    }
    @Override
    public List<User> getChatMembers(String chatName) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("chat_name", chatName);
        return namedJdbcTemplate.query(env.getProperty("getChatMembers"),
                namedParams, new FriendMapper());
    }
    @Override
    public void renameChat(String oldChatName, String newChatName) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("old_chat_name", oldChatName);
        namedParams.put("new_chat_name", newChatName);
        namedJdbcTemplate.update(env.getProperty("renameChat"), namedParams);
    }

    private class MessageMapper implements RowMapper {
        @Override
        public Object mapRow(ResultSet resultSet, int i) throws SQLException {
            Message message = new Message();
            message.setDateSend(resultSet.getDate("datetime_send"));
            message.setFromName(userManager.getUserById(resultSet.getLong("user_id")).getLogin());
            message.setMessage(resultSet.getString("messege"));
            return message;
        }
    }
    @Override
    public List<Message> getMessagesByChatName(String chatName) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("chat_name", chatName);
        return namedJdbcTemplate.query(env.getProperty("getMessagesByChatName"),
                namedParams, new MessageMapper());
    }
    @Override
    public Long getChatMemberByNameAndUserId(String chatName, Long userId){
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("chat_name", chatName);
        namedParams.put("user_id", userId);
        return namedJdbcTemplate.queryForObject(
                env.getProperty("getChatMemberByNameAndUserId"), namedParams, Long.class);
    }

    @Override
    public void saveMessage(Message message){
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("member_id", getChatMemberByNameAndUserId(
                message.getToName(),userManager.getUserByLogin(message.getFromName()).getUserId()));
        namedParams.put("messege", message.getMessage());
        namedParams.put("datetime_send", message.getDateSend());
        namedJdbcTemplate.update(env.getProperty("saveMessage"), namedParams);
    }

    @Override
    public boolean isExistChatName(String chatName){
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("chat_name", chatName);
        return namedJdbcTemplate.queryForObject(env.getProperty("isExistChatName"), namedParams, Integer.class) > 0;
    }
}
