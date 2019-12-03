package com.example.netbooks.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import com.example.netbooks.models.Message;
import com.example.netbooks.security.WebSocketConfig;
import com.example.netbooks.services.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/socket")
@CrossOrigin(origins = {"http://localhost:4200", "https://netbooksfront.herokuapp.com"})
@Import(WebSocketConfig.class)
public class WebSocketController {
    private SimpMessagingTemplate simpMessagingTemplate;
    private ChatService chatService;
    @Autowired
    public WebSocketController(SimpMessagingTemplate simpMessagingTemplate, ChatService chatService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/send/message")
    public Message useSocketCommunication(Message message) {
        message.setDateSend(new Date());
        chatService.saveMessage(message);
        this.simpMessagingTemplate.convertAndSend("/socket-publisher/" +
                message.getToName(), message);
        this.simpMessagingTemplate.convertAndSend("/socket-publisher/" +
                message.getFromName(), message);
        return message;
    }
}
