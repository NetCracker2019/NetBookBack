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

/*@GetMapping("/{login}")
    public List<User> getUser(@PathVariable("login") String login,
            @RequestParam("sought") String sought, @RequestParam("where") String where,
            @RequestParam("cnt") int cntPersons, @RequestParam("offset") int offset)*/

    @GetMapping()
    public List<Notification> getNotificationsForUser(@RequestParam("cnt")int cnt, @RequestParam("offset")int offset) {
        log.debug("Getting notification for user in {}: ", this.getClass().getName());
        long id = userManager.getUserIdByName(((UserDetails) SecurityContextHolder
                .getContext().getAuthentication()
                .getPrincipal()).getUsername());
        return notificationService.getAllViewNotificationsByUserId(id, cnt, offset);
    }//

    @GetMapping("/count")
    public ResponseEntity<?> getNotifCount() {

        long id = userManager.getUserIdByName(((UserDetails) SecurityContextHolder
                .getContext().getAuthentication()
                .getPrincipal()).getUsername());
        return ResponseEntity.ok(notificationService.getNotifCount(id));
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
    public void markNotifAsReadByNotifId(@RequestBody Notification notification){
        log.debug("Mark notification as read by notifId ");
        notificationService.markNotifAsReadByNotifId(notification);
    }




    @DeleteMapping("/delete-all")
    public void deleteAllNotificationsByUserId(){
        long userId = userManager.getUserIdByName(((UserDetails) SecurityContextHolder
                .getContext().getAuthentication()
                .getPrincipal()).getUsername());
        notificationService.deleteAllNotificationsByUserId(userId);
    }

}
