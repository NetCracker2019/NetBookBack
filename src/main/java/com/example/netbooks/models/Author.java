package com.example.netbooks.models;

public class Author {
    private long author_id;
    private String fullname;

    public long getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(long author_id) {
        this.author_id = author_id;
    }

    public Author(String fullname) {
        this.fullname = fullname;
    }

    public Author(long author_id, String fullname) {
        this.author_id = author_id;
        this.fullname = fullname;
    }
}
