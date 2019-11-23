package com.example.netbooks.dao.interfaces;

import com.example.netbooks.models.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {

    List<Notification> getAllNotificationsByUserId(long userId);

    void addNotification(Notification notification);

    void markAsRead();

    Notification getNotification(int notificationId);
}
