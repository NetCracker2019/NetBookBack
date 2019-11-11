package com.example.netbooks.models;

public class Book {

    private long bookId;
    private String title;
    private int like;
    private String imagePath;
    private String release_date;
    private String language;
    private int pages;
    private boolean approved;

    public Book(long bookId, String title, int like, String imagePath, String release_date, String language, int pages, boolean approved) {
        this.bookId = bookId;
        this.title = title;
        this.like = like;
        this.imagePath = imagePath;
        this.release_date = release_date;
        this.language = language;
        this.pages = pages;
        this.approved = approved;
    }

    public long getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }
    public int getLike() {
        return like;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getLanguage() {
        return language;
    }

    public int getPages() {
        return pages;
    }

    public boolean isApproved() {
        return approved;
    }
}