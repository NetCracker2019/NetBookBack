package com.example.netbooks.controllers;

import com.example.netbooks.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://netbooksfront.herokuapp.com"})
@RequestMapping("/notifications}")
@Slf4j
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getNotificationsForUser(@PathVariable long id) {
        log.debug("Getting notification for user in {}: ", this.getClass().getName());

        return ResponseEntity.ok(notificationService.getAllNotificationsByUserId(id));
    }


}
