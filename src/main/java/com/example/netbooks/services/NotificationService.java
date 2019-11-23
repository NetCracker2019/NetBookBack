package com.example.netbooks.services;

import com.example.netbooks.dao.interfaces.NotificationRepository;
import com.example.netbooks.models.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        NotificationTypeEnum notifType;
        NotificationBuilder notificationBuilder=new NotificationBuilder();

        switch (notifName){
            case "ADD":
                notifType=NotificationTypeEnum.ADD_FRIEND_NOTIFICATION;

                notificationRepository.addNotification(notifType.buildAddNotification(notificationBuilder,userId));

                break;
            default:
                throw new IllegalArgumentException("notification type incorect");

        }
    }

    public void markAsRead() {
        notificationRepository.markAsRead();
    }


}
