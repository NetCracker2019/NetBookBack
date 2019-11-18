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

}