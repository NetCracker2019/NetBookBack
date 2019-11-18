package com.example.netbooks.models;

public class Book {
    private long bookId;
    private String title;
    private String author;
    private String genre;
    private int like;
    private String imagePath;
    private String release_date;
    private String language;
    private int pages;
    private String description;
    private boolean approved;

    public Book(long bookId, String title, String author, String genre, int like, String imagePath, String release_date, String language, int pages, String description, boolean approved) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.like = like;
        this.imagePath = imagePath;
        this.release_date = release_date;
        this.language = language;
        this.pages = pages;
        this.description = description;
        this.approved = approved;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
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

    public String getDescription() {
        return description;
    }

    public boolean isApproved() {
        return approved;
    }
}