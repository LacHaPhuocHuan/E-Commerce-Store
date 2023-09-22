package com.huancoder.market.repository;

import com.huancoder.market.dto.ReviewDTO;
import com.huancoder.market.model.Review;
import com.huancoder.market.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByUserId(@Param("user_id") Long id);

    List<Review> findByProductId(@Param("product_id")Long productId);
}
