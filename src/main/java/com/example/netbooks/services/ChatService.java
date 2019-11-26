package com.example.netbooks.services;

import com.example.netbooks.dao.implementations.ChatRepositoryImpl;
import com.example.netbooks.models.Message;
import com.example.netbooks.models.User;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ChatService {
    ChatRepositoryImpl chatRepository;
    @Autowired
    public ChatService(ChatRepositoryImpl chatRepository) {
        this.chatRepository = chatRepository;
    }

    public List<String> getChatsByUserId(Long userId){
        return chatRepository.getChatsByUserId(userId);
    }

    public List<Message> getMessagesByChatName(String chatName) {
        return chatRepository.getMessagesByChatName(chatName);
    }

    public void saveMessage(Message message) {
        chatRepository.saveMessage(message);
    }

    public boolean isExistChatName(String chatName) {
        return chatRepository.isExistChatName(chatName);
    }

    public void createNewChat(String chatName, List<String> members) {
        chatRepository.createNewChat(chatName, members);
    }

    public List<User> getChatMembers(String chatName) {
        return chatRepository.getChatMembers(chatName);
    }

    public void renameChat(String oldChatName, String newChatName) {
        chatRepository.renameChat(oldChatName, newChatName);
    }

    public void addMembersToChat(String chatName, List<String> members) {
        chatRepository.addMembersToChat(chatName, members);
    }
}
