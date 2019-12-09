package com.example.netbooks.dao.interfaces;

import com.example.netbooks.models.Chat;
import com.example.netbooks.models.Message;
import com.example.netbooks.models.User;

import java.util.List;

public interface ChatRepository {

    List<Chat> getChatsByUserId(Long userId);
    List<Message> getMessagesByChatId(Long chatId);
    List<User> getChatMembers(Long chatId);

    void saveMessage(Message message);
    Long getChatMemberByChatIdAndUserId(Long chatId, Long userId);
    void createNewChat(String chatName, List<String> members);

    void updateChat(Long chatId, String editedChatName, List<String> addedMembers, List<String> removedMembers);
}
