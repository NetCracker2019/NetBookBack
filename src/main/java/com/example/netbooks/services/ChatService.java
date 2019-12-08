package com.example.netbooks.services;

import com.example.netbooks.dao.implementations.ChatRepositoryImpl;
import com.example.netbooks.models.Chat;
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

    public List<Chat> getChatsByUserId(Long userId){
        return chatRepository.getChatsByUserId(userId);
    }

    public List<Message> getMessagesByChatId(Long chatId) {
        return chatRepository.getMessagesByChatId(chatId);
    }

    public void saveMessage(Message message) {
        chatRepository.saveMessage(message);
    }

    public void createNewChat(String chatName, List<String> members) {
        chatRepository.createNewChat(chatName, members);
    }

    public List<User> getChatMembers(Long chatId) {
        return chatRepository.getChatMembers(chatId);
    }

    public void updateChat(Long chatId, String editedChatName, List<String> addedMembers, List<String> removedMembers) {
        addedMembers.add("");
        removedMembers.add("");
        chatRepository.updateChat(chatId, editedChatName, addedMembers, removedMembers);
    }
}
