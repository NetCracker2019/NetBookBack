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




    public void addNotification(Notification notification) {


        java.util.Date now = new java.util.Date();
        notification.setDate(new Date(now.getTime()));
        notificationRepository.addNotification(notification);

    }

    public void markAsRead() {
        notificationRepository.markAsRead();
    }


}
