package com.example.netbooks.controllers;


import com.example.netbooks.models.Chat;
import com.example.netbooks.models.Message;
import com.example.netbooks.models.User;
import com.example.netbooks.services.ChatService;
import com.example.netbooks.services.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://netbooksfront.herokuapp.com"})
@RequestMapping(value = "/chat")
@Slf4j
public class ChatController {
    private ChatService chatService;
    private UserManager userManager;
    @Autowired
    public ChatController(ChatService chatService,
                          UserManager userManager) {
        this.chatService = chatService;
        this.userManager = userManager;
    }

    //get chat list by login
    @GetMapping("/{login}/chats")
    public List<Chat> getChatsByLogin(@PathVariable("login")String login){
        return chatService.getChatsByUserId(
                userManager.getUserByLogin(login).getUserId());
    }

    //get chat messages history
    @GetMapping("/{chatId}")
    public List<Message> getMessagesByChatName(@PathVariable("chatId") Long chatId){
        return chatService.getMessagesByChatId(chatId);
    }
    @GetMapping("/{chatId}/members")
    public List<User> getChatMembers(@PathVariable("chatId") Long chatId){
        return chatService.getChatMembers(chatId);
    }

    @PostMapping("/create/{chatName}")
    public void createNewChat(@PathVariable("chatName")String chatName,
                              @RequestBody List<String> members){
        chatService.createNewChat(chatName, members);
    }


    @PutMapping("/{chatId}/update/{editedChatName}")
    public void updateChat(@PathVariable("chatId") Long chatId,
                           @RequestBody List<String> removedMembers,
                           @RequestParam("addedMembers") List<String> addedMembers,
                           @PathVariable("editedChatName") String editedChatName){
        chatService.updateChat(chatId, editedChatName, addedMembers, removedMembers);
    }

}
