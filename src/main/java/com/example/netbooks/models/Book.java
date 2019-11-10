package com.example.netbooks.models;

import java.util.Date;

public class Book {

    private long bookId;
    private String title;
    private long likes;
    private String imagePath;
    private Date releaseDate;
    private String lang;
    private int pages;
    private boolean approved;

    public Book(long bookId, String title, long likes, String imagePath,
                Date releaseDate, String lang, int pages, boolean approved) {
        this.bookId = bookId;
        this.title = title;
        this.likes = likes;
        this.imagePath = imagePath;
        this.releaseDate = releaseDate;
        this.lang = lang;
        this.pages = pages;
        this.approved = approved;
    }


    public long getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public long getLikes() {
        return likes;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getLang() {
        return lang;
    }

    public int getPages() {
        return pages;
    }

    public boolean isApproved() {
        return approved;
    }
}