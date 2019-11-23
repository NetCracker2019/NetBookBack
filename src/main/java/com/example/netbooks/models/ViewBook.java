package com.example.netbooks.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@Getter
@Setter
public class ViewBook {
    private long bookId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("authors")
    private String[] authors;
    private long likes;
    @JsonProperty("image_path")
    private String imagePath;
    private Date releaseDate;
    private String lang;
    private int pages;
    private String[] genres;
    private String description;

    public ViewBook() {}
}
