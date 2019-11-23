package com.example.netbooks.services;

import com.example.netbooks.dao.interfaces.NotificationRepository;
import com.example.netbooks.models.Notification;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;

public enum NotificationTypeEnum {
    ADD_FRIEND_NOTIFICATION {
        public Notification buildAddNotification(NotificationBuilder notifBuilder, int userId){
            notifBuilder.setUserId(userId);
            notifBuilder.setNotifName("ADD");
            notifBuilder.setNotifTitle("Add to frind request");
            notifBuilder.setNotifText("Check your friends-page somebody want to add you!)");
            notifBuilder.setDate(LocalDate.now());
            notifBuilder.setTime(LocalTime.now());
            notifBuilder.setIsRead(false);
            return notifBuilder.getResult();
        }
    };

    public Notification buildAddNotification(NotificationBuilder notificationBuilder, int userId) {
    }
}
