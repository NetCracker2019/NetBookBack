package com.example.netbooks.dao.implementations;

import com.example.netbooks.dao.interfaces.ChatRepository;
import com.example.netbooks.dao.mappers.ChatMapper;
import com.example.netbooks.dao.mappers.FriendMapper;
import com.example.netbooks.models.Chat;
import com.example.netbooks.models.Message;
import com.example.netbooks.models.User;
import com.example.netbooks.services.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:queries/chat.properties")
@Repository
@Slf4j
public class ChatRepositoryImpl implements ChatRepository {
    private Environment env;
    private DataSource dataSource;
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    private UserManager userManager;

    @Autowired
    public ChatRepositoryImpl(Environment env,
                              NamedParameterJdbcTemplate namedJdbcTemplate,
                              UserManager userManager, DataSource dataSource) {
        this.env = env;
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.userManager = userManager;
        this.dataSource = dataSource;
    }

    @Override
    public List<Chat> getChatsByUserId(Long userId){
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_id", userId);
        return namedJdbcTemplate.query(env.getProperty("getChatsByUserId"),
                namedParams, new ChatMapper());
    /*
        SimpleJdbcCall jdbcCall = new
                SimpleJdbcCall(dataSource).withFunctionName("get_chats_by_userid");
                //.returningResultSet("result",new ChatMapper());

        SqlParameterSource in = new MapSqlParameterSource()
               .addValue("userId", userId);
        log.info("sdg {}", jdbcCall.execute(in));
        //Map<String,Object> result = jdbcCall.execute(in);
        //List<Chat> chats = (List<Chat>) result.get("result");
        //return chats;
        //return jdbcCall.execute( userId);
        return null;*/
    }
    @Override
    public void createNewChat(String chatName, List<String> members) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("chat_name", chatName);
        namedParams.put("members", members);
        namedJdbcTemplate.update(env.getProperty("createNewChat"), namedParams);
    }

    @Override
    public List<User> getChatMembers(Long chatId) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("chat_id", chatId);
        return namedJdbcTemplate.query(env.getProperty("getChatMembers"),
                namedParams, new FriendMapper());
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
    public List<Message> getMessagesByChatId(Long chatId) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("chat_id", chatId);
        return namedJdbcTemplate.query(env.getProperty("getMessagesByChatId"),
                namedParams, new MessageMapper());
    }
    @Override
    public Long getChatMemberByChatIdAndUserId(Long chatId, Long userId){
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("chat_id", chatId);
        namedParams.put("user_id", userId);
        return namedJdbcTemplate.queryForObject(
                env.getProperty("getChatMemberByChatIdAndUserId"), namedParams, Long.class);
    }

    @Override
    public void saveMessage(Message message){
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("member_id", getChatMemberByChatIdAndUserId(
                message.getToId(), userManager.getUserByLogin(message.getFromName()).getUserId()));
        namedParams.put("messege", message.getMessage());
        namedParams.put("datetime_send", message.getDateSend());
        namedJdbcTemplate.update(env.getProperty("saveMessage"), namedParams);
    }

    @Override
    public void updateChat(Long chatId, String editedChatName, List<String> addedMembers, List<String> removedMembers){
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("chat_name", editedChatName);
        namedParams.put("chat_id", chatId);
        namedParams.put("addedMembers", addedMembers);
        namedParams.put("removedMembers", removedMembers);
        namedJdbcTemplate.update(env.getProperty("updateChat"), namedParams);
    }

}
