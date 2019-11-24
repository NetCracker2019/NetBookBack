package com.example.netbooks.models;

import lombok.*;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Notification {

    private int notificationId;
    private int userId;
    private String notifName;
    private String notifTitle;
    private String notifText;
    private String date;
    private boolean isRead;

    public Notification(int userId, String notifName, String notifTitle, String notifText, String date, boolean isRead) {
        this.userId = userId;
        this.notifName = notifName;
        this.notifTitle = notifTitle;
        this.notifText =notifText;
        this.date =date;
        this.isRead=isRead;
    }

    public Notification getResult() {
        return new Notification(userId, notifName, notifTitle, notifText, date, isRead);
    }

    public boolean getIsRead() {
        return isRead;
    }
}
