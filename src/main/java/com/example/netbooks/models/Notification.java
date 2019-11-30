package com.example.netbooks.models;

import lombok.*;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class Notification {

    private Integer notificationId;
    private Integer userId;
    private Date date;
    private boolean isRead;
    private Integer fromUserId;
    private Integer notifTypeId;
    private Integer overviewId;
    private Integer reviewId;
    private Integer bookId;
    private Integer achievId;
    private String overviewName;
    private String reviewName;
    private String fromUserName;
    private String bookName;
    private String achievName;
    private String notifTitle;
    private String notifText;

    public Notification() {
        super();
    }

    public Notification(Integer userId, Date date, boolean isRead, Integer fromUserId, Integer notifTypeId,
                        Integer overviewId, Integer reviewId, Integer bookId, Integer achievId) {
        this.userId = userId;
        this.date = date;
        this.isRead = isRead;
        this.fromUserId = fromUserId;
        this.notifTypeId = notifTypeId;
        this.overviewId = overviewId;
        this.reviewId = reviewId;
        this.bookId = bookId;
        this.achievId = achievId;
        this.overviewName = null;
        this.reviewName = null;
        this.bookName = null;
        this.achievName = null;
    }

    public Notification(int notifTypeId, String overviewName,String reviewName,String fromUserName,String bookName,String achievName,String notifTitle,String notifText) {
        this.notifTypeId = notifTypeId;
        this.overviewName = overviewName;
        this.reviewName = reviewName;
        this.bookName = bookName;
        this.achievName = achievName;
        this.fromUserName=fromUserName;
        this.notifTitle=notifTitle;
        this.notifText=notifText;

    }


    public boolean getIsRead() {
        return isRead;
    }
}
