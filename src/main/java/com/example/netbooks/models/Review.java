package com.example.netbooks.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Review {
    long reviewId;
    long userId;
    long bookId;
    String userName;
    String userAvatarPath;
    String reviewText;
    int rating;
    boolean approved;

    public Review(){}
}
