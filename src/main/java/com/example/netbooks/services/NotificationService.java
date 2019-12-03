package com.example.netbooks.services;

import com.example.netbooks.dao.interfaces.NotificationRepository;
import com.example.netbooks.models.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    public List<Notification> getAllNotificationsByUserId(long userId) {
        return notificationRepository.getAllNotificationsByUserId(userId);
    }

    public void addNotification(int userId, NotificationEnum notifName ) throws IllegalArgumentException{
        Notification notification;
        switch (notifName){
            case ADD_FRIEND_NOTIF:
                java.util.Date now = new java.util.Date();
                notification= new Notification(userId,"addFriend","Friend Request","check your frinds somebody want to add you", new Date(now.getTime()),false);
                notificationRepository.addNotification(notification.getResult());
                break;
            default:
                throw new IllegalArgumentException("notification type incorect");

        }
    }

    public void markAsRead() {
        notificationRepository.markAsRead();
    }


}
