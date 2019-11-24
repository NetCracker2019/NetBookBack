package com.example.netbooks.models;

import lombok.Data;

@Data
public class Author {
    private long authorId;
    private String fullName;

    public Author(long authorId, String fullName) {
        this.authorId = authorId;
        this.fullName = fullName;
    }
}
