package com.example.netbooks.dao.interfaces;

import com.example.netbooks.models.Message;
import com.example.netbooks.models.User;

import java.util.List;

public interface ChatRepository {

    List<String> getChatsByUserId(Long userId);
    List<Message> getMessagesByChatName(String chatName);
    List<User> getChatMembers(String chatName);

    void saveMessage(Message message);
    Long getChatMemberByNameAndUserId(String chatName, Long userId);
    boolean isExistChatName(String chatName);
    void createNewChat(String chatName, List<String> members);
    void addMembersToChat(String chatName, List<String> members);
    void renameChat(String oldChatName, String newChatName);
}
