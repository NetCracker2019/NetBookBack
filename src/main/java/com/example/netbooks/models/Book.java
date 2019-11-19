package com.example.netbooks.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
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


}
