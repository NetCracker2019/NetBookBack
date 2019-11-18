package com.example.netbooks.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class ShortBookDescription {
    @JsonProperty("title")
    private String title;
    @JsonProperty("image_path")
    private String imagePath;
    @JsonProperty("authors")
    private String[] authors;
    @JsonProperty("likes")
    private int likes;
}
