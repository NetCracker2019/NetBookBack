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

    public Book(long bookId, String title, long likes, String imagePath, Date releaseDate, String lang, int pages, boolean approved) {
        this.bookId = bookId;
        this.title = title;
        this.imagePath = imagePath;
        this.likes = likes;
        this.releaseDate = releaseDate;
        this.lang = lang;
        this.pages = pages;
        this.approved = approved;
    }


    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }


    public long getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", releaseDate=" + releaseDate +
                '}';
    }

    public long getLikes() {
        return likes;
    }
}