package com.example.netbooks.services;

import com.example.netbooks.dao.implementations.NotificationRepositoryImpl;
import com.example.netbooks.dao.interfaces.NotificationRepository;
import com.example.netbooks.models.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NotificationService {
//TODO add dependency injection
    @Autowired
    NotificationRepository notificationRepository ;

    public List<Notification> getAllNotificationsByUserId(long userId){
        return notificationRepository.getAllNotificationsByUserId(userId);
    }

    public void addNotification(Notification notification){
        switch (not.type) {
            case 0 :
                notifactiosa = new Notif()6
        }
        notificationRepository.addNotification(notification);
    }

    public void markAsRead(int notificationId){
        notificationRepository.markAsRead(notificationId);
    }

    public Optional<Notification> getNotification(long id){
       return notificationRepository.getNotification(id);
    }

}
