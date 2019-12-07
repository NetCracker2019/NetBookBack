package com.example.netbooks.services;

import com.example.netbooks.dao.interfaces.NotificationRepository;
import com.example.netbooks.models.Notification;
import com.example.netbooks.models.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Service
@Slf4j
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    UserManager userManager;

    public List<Notification> getAllNotificationsByUserId(long userId) {
        return notificationRepository.getAllNotificationsByUserId(userId);
    }


    public List<Notification> getAllViewNotificationsByUserId(long userId) {
        List<Notification> notifList = notificationRepository.getAllViewNotificationsByUserId(userId);
        for (Notification notif : notifList) {
            notifList.set(notifList.indexOf(notif), parseViewNotif(notif));
        }
        return notifList;
    }

    public List<Notification> getAllUnreadViewNotificationsByUserId(long userId) {
        List<Notification> notifList = notificationRepository.getAllUnreadViewNotificationsByUserId(userId);
        for (Notification notif : notifList) {
            notifList.set(notifList.indexOf(notif), parseViewNotif(notif));
        }
        return notifList;
    }

    public List<Notification> getAllViewNotificationsByUserIdAndTypeId(long userId, long notif_type_id) {
        List<Notification> notifList = notificationRepository.getAllViewNotificationsByUserIdAndTypeId(userId, notif_type_id);
        for (Notification notif : notifList) {
            notifList.set(notifList.indexOf(notif), parseViewNotif(notif));
        }
        return notifList;
    }

    public void createAndSaveReviewNotif(long fromUserId, List<User> friends, long bookId, long reviewId) {
        for (User user : friends) {
            Notification notification = new Notification();
            notification.setNotifTypeId(4);
            notification.setUserId((int) userManager.getUserIdByName(user.getLogin()));
            notification.setFromUserId((int) fromUserId);
            notification.setBookId((int) bookId);
            notification.setReviewId((int) reviewId);
            addNotification(notification);
        }
    }

    public Notification parseViewNotif(Notification notif) {
        String notifText = notif.getNotifText();
        notifText = notifText.replaceAll("user_name", notif.getFromUserName());
        notifText = notifText.replaceAll("book_name", notif.getBookName());
        notifText = notifText.replaceAll("achiev_name", notif.getAchievName());
        notif.setNotifText(notifText);
        return notif;
    }

    public void addNotification(Notification notification) {

        java.util.Date now = new java.util.Date();
        notification.setDate(new Date(now.getTime()));
        notificationRepository.addNotification(notification);
    }

    public void markAllAsRead(long id) {
        notificationRepository.markAllAsRead(id);
    }

    public void markNotifAsReadByNotifId(Notification notification) {
        Integer id = notification.getNotificationId();
        notificationRepository.markNotifAsReadByNotifId(id);
    }


}
