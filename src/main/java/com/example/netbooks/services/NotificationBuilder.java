package com.example.netbooks.services;

import com.example.netbooks.models.Notification;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
public class NotificationBuilder {
    private int userId;
    private String notifName;
    private String notifTitle;
    private String notifText;
    private LocalDate date;
    private LocalTime time;
    private boolean isRead;



    public Notification getResult(){
        return new Notification(userId,notifName,notifTitle,notifText,date,time,isRead);
    }
}
