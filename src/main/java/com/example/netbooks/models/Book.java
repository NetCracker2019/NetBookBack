package com.example.netbooks.models;

public class Book {

    private long bookId;
    private String title;

    public Book(long book_id, String title) {
        this.bookId = book_id;
        this.title = title;
    }

    public long getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }
}