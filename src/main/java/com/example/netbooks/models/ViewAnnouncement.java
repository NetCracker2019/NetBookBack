package com.example.netbooks.models;


import lombok.AllArgsConstructor;
import lombok.Data;
//todo модификаторы доступа
@Data
@AllArgsConstructor
public class ViewAnnouncement {
    public int getAnnouncmentId() {
        return announcmentId;
    }

    int announcmentId;
    String releaseDate;
    String title;
    String description;
    String imagePath;
    String[] authors;
    String[] genres;
}
