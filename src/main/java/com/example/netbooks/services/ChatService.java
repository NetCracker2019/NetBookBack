package com.example.netbooks.services;

import com.example.netbooks.dao.implementations.ChatRepositoryImpl;
import com.example.netbooks.models.Chat;
import com.example.netbooks.models.Message;
import com.example.netbooks.models.User;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@Slf4j
public class ChatService {
    private ChatRepositoryImpl chatRepository;
    @Autowired
    public ChatService(ChatRepositoryImpl chatRepository) {
        this.chatRepository = chatRepository;
    }

    public List<Chat> getChatsByUserId(Long userId) {
        return chatRepository.getChatsByUserId(userId);
    }

    public List<Message> getMessagesByChatId(Long chatId) {
        return chatRepository.getMessagesByChatId(chatId);
    }

    public void saveMessage(Message message) {
        chatRepository.saveMessage(message);
    }

    public void createNewChat(String chatName, List<String> members) throws SQLException {
        chatRepository.createNewChat(chatName, members);
    }

    public List<User> getChatMembers(Long chatId) {
        return chatRepository.getChatMembers(chatId);
    }

    public void updateChat(Long chatId, String editedChatName,
                           List<String> addedMembers,
                           List<String> removedMembers,
                           String chatAvatar) throws SQLException {
        addedMembers.add("");
        removedMembers.add("");
        chatRepository.updateChat(chatId, editedChatName, addedMembers, removedMembers, chatAvatar);
    }

    public boolean isMemberOfChat(Long chatId, String login){
        List<User> members = chatRepository.getChatMembers(chatId);
        for(User member: members){
            if(member.getLogin().equals(login))
                return true;
        }
        return false;
    }
}
