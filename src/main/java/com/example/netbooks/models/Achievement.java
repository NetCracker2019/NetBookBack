package com.example.netbooks.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Achievement {
    private Long  achievementId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("cntBook")
    private int cntBook;
    @JsonProperty("image_path")
    private String imagePath;
}
