package com.example.netbooks.models;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class ViewBook {
    private long bookId;
    private String title;
    private String[] authors;
    private long likes;
    private String imagePath;
    private Date releaseDate;
    private String lang;
    private int pages;
    private String[] genres;
    private String description;
}
