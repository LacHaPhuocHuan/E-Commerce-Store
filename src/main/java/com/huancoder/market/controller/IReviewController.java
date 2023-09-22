package com.huancoder.market.controller;

import com.huancoder.market.dto.ReviewDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RequestMapping("api/v1/reviews")
public interface IReviewController {
    //
    @GetMapping("")
    public ResponseEntity<?> getAllReviewsByUser();
    @GetMapping("/u")
    public ResponseEntity<?> findReviewsByUser();
    @GetMapping("/p/{product_id}")
    public ResponseEntity<?> getReviewsByProductId(@PathVariable("product_id") Long productId);
    @PostMapping("")
    public ResponseEntity<?> addReview(@RequestBody ReviewDTO dto);
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@RequestBody ReviewDTO dto,@PathVariable("id") Long id);
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReviewById(@PathVariable("id") Long id) throws AccessDeniedException;

}
