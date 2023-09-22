package com.huancoder.market.service.serviceImpl;

import com.huancoder.market.dto.ReviewDTO;
import com.huancoder.market.model.Review;
import com.huancoder.market.model.User;
import com.huancoder.market.repository.ProductRepository;
import com.huancoder.market.repository.ReviewRepository;
import com.huancoder.market.security.Role;
import com.huancoder.market.service.IReviewService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.AccessException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ReviewServiceImpl implements IReviewService {
    @Autowired
    ReviewRepository repository;
    @Autowired
    ModelMapper mapper;
    @Autowired
    ProductRepository productRepository;
    @Override
    public List<ReviewDTO> getAllReviewsByUser(User currentUser) {
        List<Review> reviews=repository.findByUserId(currentUser.getId());
        return reviews.stream().map(r->mapper.map(r, ReviewDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> getAllReviews() {
        List<Review> reviews=repository.findAll();
        return reviews.stream().map(r->mapper.map(r, ReviewDTO.class)).collect(Collectors.toList());
    }

    @Override
    public ReviewDTO addReviews(ReviewDTO dto, User currentUser) {
        if(!checkValidReview(dto))
            throw new IllegalArgumentException("Added Review failed.");
        if(!productRepository.existsById(dto.getProductId()))
            throw new IllegalArgumentException("Added Review failed. Product don't exist.");
        Review review=mapper.map(dto, Review.class);
        review.setUser(currentUser);
        review.setProduct(productRepository.findById(dto.getProductId()).orElseThrow());
        review.setId(null);
        var addedReview=repository.save(review);
        return mapper.map(addedReview, ReviewDTO.class);
    }

    @Override
    public ReviewDTO updateReviews(ReviewDTO dto, Long id) {
        if(!repository.existsById(id))
            throw new NoSuchElementException("Review don't exist.");
        var review=repository.findById(id).orElseThrow();
        review.setContent(dto.getContent());
        review.setImgs(dto.getImgs());
        review.setScore(dto.getScore());
        review.setEvaluation(dto.getEvaluation());
        review.setCreatedAt(dto.getCreatedAt());
        var updatedReview= repository.save(review);
        return mapper.map(updatedReview, ReviewDTO.class);
    }

    @Override
    public void deleteReview(Long id, User currentUser) throws AccessDeniedException {
        if(!repository.existsById(id))
            throw new NoSuchElementException("Review don't exist.");
        var reviews= repository.findById(id).orElseThrow();
        if(!reviews.getUser().getId().equals(currentUser.getId())
                && !currentUser.getRole().equals(Role.ADMIN)
        ){
            log.info("CREATED USER : {} ",reviews.getUser().getId());
            log.info("CURRENT USER: {}", currentUser.getId());
            log.info("CURRENT USER have ROLE: {}", currentUser.getRole());
            throw new AccessDeniedException("User haven't authority for delete this review.");
        }
        repository.delete(reviews);
    }

    @Override
    public List<ReviewDTO> getReviewByProductId(Long productId) {
        List<Review> reviews=repository.findByProductId(productId);
        return reviews.stream().map(r->mapper.map(r, ReviewDTO.class)).collect(Collectors.toList());
    }

    private boolean checkValidReview(ReviewDTO dto) {
        if(Objects.isNull(dto)
        || Objects.isNull(dto.getProductId())
        || dto.getProductId()==0)
            return false;
        return true;

    }
}
