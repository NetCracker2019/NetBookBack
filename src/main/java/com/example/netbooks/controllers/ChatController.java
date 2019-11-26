package com.example.netbooks.controllers;


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
    public List<String> getChatsByLogin(@PathVariable("login")String login){
        return chatService.getChatsByUserId(
                userManager.getUserByLogin(login).getUserId());
    }

    //get chat messages history
    @GetMapping("/{chatName}")
    public List<Message> getMessagesByChatName(@PathVariable("chatName")String chatName){
        return chatService.getMessagesByChatName(chatName);
    }
    @GetMapping("/is-exist/{chatName}")
    public boolean isExistChatName(@PathVariable("chatName")String chatName){
        return chatService.isExistChatName(chatName);
    }
    @GetMapping("/{chatName}/members")
    public List<User> getChatMembers(@PathVariable("chatName")String chatName){
        return chatService.getChatMembers(chatName);
    }

    @PostMapping("/create/{chatName}")
    public void createNewChat(@PathVariable("chatName")String chatName,
                              @RequestBody List<String> members){
        log.info("chatName {}", chatName);
        log.info("chatmemb {}", members);
        chatService.createNewChat(chatName, members);
    }

    @PutMapping("/rename/{chatName}")
    public void renameChat(@PathVariable("chatName")String oldChatName,
                           @RequestBody String newChatName){
        log.info("chatName {}", oldChatName);
        log.info("chatName new {}", newChatName);
        chatService.renameChat(oldChatName, newChatName);
    }

    @PutMapping("/{chatName}/add-members")
    public void addMembersToChat(@PathVariable("chatName")String chatName,
                                 @RequestBody List<String> members){
        log.info("chatName {}", chatName);
        log.info("chatmemb {}", members);
        chatService.addMembersToChat(chatName, members);
    }


}
