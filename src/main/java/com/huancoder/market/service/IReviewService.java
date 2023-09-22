package com.huancoder.market.service;

import com.huancoder.market.dto.ReviewDTO;
import com.huancoder.market.model.User;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface IReviewService {
    List<ReviewDTO> getAllReviewsByUser(User currentUser);
    List<ReviewDTO> getAllReviews();

    ReviewDTO addReviews(ReviewDTO dto, User currentUser);

    ReviewDTO updateReviews(ReviewDTO dto, Long id);

    void deleteReview(Long id, User currentUser) throws AccessDeniedException;

    List<ReviewDTO> getReviewByProductId(Long productId);
}
