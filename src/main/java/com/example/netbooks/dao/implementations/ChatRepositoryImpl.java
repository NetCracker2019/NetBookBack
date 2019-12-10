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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:queries/chat.properties")
@Repository
@Slf4j
public class ChatRepositoryImpl implements ChatRepository {
    private DataSource dataSource;
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    private UserManager userManager;

    @Autowired
    public ChatRepositoryImpl(NamedParameterJdbcTemplate namedJdbcTemplate,
                              UserManager userManager, DataSource dataSource) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.userManager = userManager;
        this.dataSource = dataSource;
    }

    @Value("${getChatsByUserId}")
    private String getChatsByUserId;

    @Value("${createNewChat}")
    private String createNewChat;

    @Value("${getChatMembers}")
    private String getChatMembers;

    @Value("${getMessagesByChatId}")
    private String getMessagesByChatId;

    @Value("${setMinRefreshDate}")
    private String setMinRefreshDate;

    @Value("${saveMessage}")
    private String saveMessage;

    @Value("${updateChat}")
    private String updateChat;

    @Override
    public List<Chat> getChatsByUserId(Long userId) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("user_id", userId);
        return namedJdbcTemplate.query(getChatsByUserId, namedParams, new ChatMapper());
    }
    @Override
    public void createNewChat(String chatName, List<String> members) throws SQLException {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("chat_name", chatName);
        namedParams.put("members", dataSource.getConnection().createArrayOf("text", members.toArray()));
        namedJdbcTemplate.queryForObject(createNewChat, namedParams, String.class);

        //SimpleJdbcCall jdbcCall = new
        //        SimpleJdbcCall(dataSource).withFunctionName("create_new_chat");
        //SqlParameterSource in = new MapSqlParameterSource()
        //        .addValue("chatname", chatName)
        //        .addValue("members",
        //                dataSource.getConnection().createArrayOf("text", members.toArray()));
        //jdbcCall.execute(in);
        //-------------------------------------------------------------------------------------------
        ///CallableStatement stmt = dataSource.getConnection().prepareCall("select * from create_new_chat(?, ?)");
        //stmt.setArray(2,
        //        dataSource.getConnection().createArrayOf("text", members.toArray()));
        //stmt.setString(1, chatName);
        //stmt.execute();
        //--------------------------------------------------------
    }

    @Override
    public List<User> getChatMembers(Long chatId) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("chat_id", chatId);
        return namedJdbcTemplate.query(getChatMembers,
                namedParams, new FriendMapper());
    }

    private class MessageMapper implements RowMapper {
        @Override
        public Object mapRow(ResultSet resultSet, int i) throws SQLException {
            Message message = new Message();
            message.setDateSend(resultSet.getDate("datetime_send"));
            message.setFromName(resultSet.getString("login"));
            message.setMessage(resultSet.getString("messege"));
            return message;
        }
    }
    @Override
    public List<Message> getMessagesByChatId(Long chatId) {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("chat_id", chatId);
        return namedJdbcTemplate.query(getMessagesByChatId, namedParams, new MessageMapper());
    }

    @Override
    public void saveMessage(Message message){
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("chat_id", message.getToId());
        namedParams.put("user_login", message.getFromName());
        namedParams.put("messege", message.getMessage());
        namedParams.put("datetime_send", message.getDateSend());
        namedJdbcTemplate.queryForObject(saveMessage, namedParams, String.class);
    }

    @Override
    public void updateChat(Long chatId, String editedChatName, List<String> addedMembers, List<String> removedMembers) throws SQLException {
        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("chat_name", editedChatName);
        namedParams.put("chat_id", chatId);
        namedParams.put("addedMembers", dataSource.getConnection()
                .createArrayOf("text", addedMembers.toArray()));
        namedParams.put("removedMembers", dataSource.getConnection()
                .createArrayOf("text", removedMembers.toArray()));
        namedJdbcTemplate.queryForObject(updateChat, namedParams, String.class);
    }

}
