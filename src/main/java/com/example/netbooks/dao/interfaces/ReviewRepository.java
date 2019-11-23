package com.example.netbooks.dao.interfaces;

import com.example.netbooks.models.Review;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository {
    List<Review> getReviewsByBookId(long id);
}
