package com.example.netbooks.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Data
@Getter
@Setter
public class Book {
    private long bookId;
    private String title;
    private ArrayList<String> authors;
    private ArrayList<String> genres;
    private int like;
    private String imagePath;
    private String release_date;
    private String language;
    private int pages;
    private String description;
    private boolean approved;


    public long getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getAuthor() {
        return authors;
    }

    public ArrayList<String> getGenre() {
        return genres;
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


    public Book(long bookId, String title, ArrayList<String> authors, ArrayList<String> genre, int like, String imagePath, String release_date, String language, int pages, String description, boolean approved) {
        this.bookId = bookId;
        this.title = title;
        this.authors = authors;
        this.genres = genre;
        this.like = like;
        this.imagePath = imagePath;
        this.release_date = release_date;
        this.language = language;
        this.pages = pages;
        this.description = description;
        this.approved = approved;
    }


}
