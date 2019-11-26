package com.example.netbooks.dao.interfaces;

import com.example.netbooks.models.Review;
import com.example.netbooks.models.ViewBook;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository {
    List<Review> getReviewsByBookId(long id);
    List<Review> getPeaceOfReviewByBook(int bookId, int page, int offset);
    int countReviews();
    boolean addReviewForUserBook(Review review);
    boolean approveReview(long reviewId);
    boolean cancelReview(long reviewId);
}
