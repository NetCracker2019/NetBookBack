package com.example.netbooks.services;

import com.example.netbooks.dao.interfaces.NotificationRepository;
import com.example.netbooks.models.Notification;
import com.example.netbooks.models.User;
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

    public List<Notification> getAllViewNotificationsByUserId(long userId) {
        return notificationRepository.getAllViewNotificationsByUserId(userId);
    }

    public List<Notification> getAllUnreadViewNotificationsByUserId(long userId) {
        return notificationRepository.getAllUnreadViewNotificationsByUserId(userId);
    }

    public List<Notification> getAllViewNotificationsByUserIdAndTypeId(long userId, long notif_type_id) {
        return notificationRepository.getAllViewNotificationsByUserIdAndTypeId(userId, notif_type_id);
    }

    public void createAndSaveReviewNotif(long fromUserId, List<User> friends, long bookId, long reviewId) {
        for (User user : friends) {
            Notification notification = new Notification();
            notification.setNotifTypeId(4);
            notification.setUserId((int) user.getUserId());
            notification.setFromUserId((int) fromUserId);
            notification.setBookId((int) bookId);
            notification.setReviewId((int) reviewId);
            addNotification(notification);
        }
    }

    public Notification parseViewNotif(Notification notif) {

        return notif;
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
