package com.example.netbooks.controllers;

import com.example.netbooks.models.Notification;
import com.example.netbooks.services.NotificationService;
import com.example.netbooks.services.UserManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://netbooksfront.herokuapp.com"})
@RequestMapping("/notifications")
@Slf4j
public class NotificationController {



    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserManager userManager;

   // @GetMapping("/")
  // public ResponseEntity<?> getNotificationsForUser() {
  //     log.debug("Getting notification for user in {}: ", this.getClass().getName());
  //     String username=((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

// <<<<<<< HEAD
//         return ResponseEntity.ok(notificationService.getAllViewNotificationsByUserId(id));
// =======
 //       return ResponseEntity.ok(notificationService.getAllNotificationsByUserId(userManager.getUserByLogin(username).getUserId()));
//
 //   }

//    @GetMapping("/user/{type}")
//    public ResponseEntity<?> getNotificationsForUserByType(@PathVariable long type) {
//        log.debug("Getting notification for user in {}: ", this.getClass().getName());
//        long id = userManager.getUserIdByName(((UserDetails) SecurityContextHolder
//                .getContext().getAuthentication()
//                .getPrincipal()).getUsername());
//        return ResponseEntity.ok(notificationService.getAllViewNotificationsByUserIdAndTypeId(id, type));
//    }

    @GetMapping()
    public ResponseEntity<?> getNotificationsForUser() {
        log.debug("Getting notification for user in {}: ", this.getClass().getName());
        long id = userManager.getUserIdByName(((UserDetails) SecurityContextHolder
                .getContext().getAuthentication()
                .getPrincipal()).getUsername());
        return ResponseEntity.ok(notificationService.getAllViewNotificationsByUserId(id));
    }

    @PutMapping("/mark")
    public void markAllAsRead(){
        log.debug("Mark notification as read for user  {}: ", this.getClass().getName());

        long userId = userManager.getUserIdByName(((UserDetails) SecurityContextHolder
                .getContext().getAuthentication()
                .getPrincipal()).getUsername());
        notificationService.markAllAsRead(userId);
    }

    @PutMapping("mark-one")
    public void markNotifAsReadByNotifId(@RequestParam int notifId){
        log.debug("Mark notification as read by notifId ");
        notificationService.markNotifAsReadByNotifId(notifId);
    }

}
