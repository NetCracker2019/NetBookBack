package com.example.netbooks.services;

import com.example.netbooks.dao.interfaces.NotificationRepository;
import com.example.netbooks.models.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void addNotification(int userId,String notifName) throws IllegalArgumentException{
        Notification notification = new Notification();
        switch (notifName){
            case "addFriend":
                notification.setUserId(userId);
                notification.setNotifName("addFriend");
                notification.setNotifTitle("Add to frind request");
                notification.setNotifText("Check your friends-page somebody want to add you!)");
                notification.setDate(LocalDate.now());
                notification.setTime(LocalTime.now());
                notification.setRead(false);
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
