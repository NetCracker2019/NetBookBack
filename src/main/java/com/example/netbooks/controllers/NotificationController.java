package com.example.netbooks.controllers;

import com.example.netbooks.services.NotificationService;
import com.example.netbooks.services.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://netbooksfront.herokuapp.com"})
@RequestMapping("/notifications}")
@Slf4j
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserManager userManager;

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getNotificationsForUser(@PathVariable long id) {
        log.debug("Getting notification for user in {}: ", this.getClass().getName());

        return ResponseEntity.ok(notificationService.getAllViewNotificationsByUserId(id));
    }

    @GetMapping("/user/{type}")
    public ResponseEntity<?> getNotificationsForUserByType(@PathVariable long type) {
        log.debug("Getting notification for user in {}: ", this.getClass().getName());
        long id = userManager.getUserIdByName(((UserDetails) SecurityContextHolder
                .getContext().getAuthentication()
                .getPrincipal()).getUsername());
        return ResponseEntity.ok(notificationService.getAllViewNotificationsByUserIdAndTypeId(id, type));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getNotificationsForUser() {
        log.debug("Getting notification for user in {}: ", this.getClass().getName());
        long id = userManager.getUserIdByName(((UserDetails) SecurityContextHolder
                .getContext().getAuthentication()
                .getPrincipal()).getUsername());
        return ResponseEntity.ok(notificationService.getAllNotificationsByUserId(id));
    }

}
