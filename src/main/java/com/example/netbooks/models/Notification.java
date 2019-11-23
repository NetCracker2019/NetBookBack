package com.example.netbooks.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    private int notificationId;
    private int userId;
    private String notifName;
    private String notifTitle;
    private String notifText;
    private LocalDate date;
    private LocalTime time;
    private boolean isRead;


}
