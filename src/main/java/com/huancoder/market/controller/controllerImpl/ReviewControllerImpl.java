package com.huancoder.market.controller.controllerImpl;

import com.huancoder.market.controller.IReviewController;
import com.huancoder.market.dto.Evaluation;
import com.huancoder.market.dto.ReviewDTO;
import com.huancoder.market.dto.common.StatusEnum;
import com.huancoder.market.dto.common.SuccessResponse;
import com.huancoder.market.security.MyUserDetail;
import com.huancoder.market.security.MyUserDetailService;
import com.huancoder.market.service.IReviewService;
import com.huancoder.market.utils.HateoasUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ReviewControllerImpl implements IReviewController {
    @Autowired
    IReviewService service;
    @Autowired
    MyUserDetailService userDetailService;
    @Override
    public ResponseEntity<?> getAllReviewsByUser() {
        List<ReviewDTO> reviews=getAllReviewsOnCacheOrDB();
        List<EntityModel<ReviewDTO>> entityModelList=reviews.stream().map(r-> {
            EntityModel<ReviewDTO> reviewDTOEntityModel= EntityModel.of(r);
            reviewDTOEntityModel=HateoasUtils.convertEntityModelRelBase(reviewDTOEntityModel,IReviewController.class, r.getId().toString());
            return reviewDTOEntityModel;
        }).collect(Collectors.toList());
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .status(StatusEnum.OK)
                        .message("These are all reviews !")
                        .data(entityModelList)
                        .build()
        );
    }

    private List<ReviewDTO> getAllReviewsOnCacheOrDB() {
        List<?> reviews=service.getAllReviews();
        if(reviews.stream().allMatch(r->r instanceof LinkedHashMap)){
            reviews=reviews.stream()
                    .map(r->{
                        LinkedHashMap<String, Object> reviewMap= (LinkedHashMap<String, Object>) r;
                        return ReviewDTO.builder()
                                .imgs(Arrays.asList(reviewMap.get("imgs").toString()))
                                .score(Integer.parseInt(reviewMap.get("score").toString()))
                                .content(reviewMap.get("content").toString())
                                .userId(Long.parseLong(reviewMap.get("userId").toString()))
                                .evaluation(Evaluation.valueOf(reviewMap.get("evaluation").toString()))
                                .createdAt(LocalDateTime.parse(reviewMap.get("createdAt").toString()))
                                .id(Long.parseLong(reviewMap.get("id").toString()))
                                .productId(Long.parseLong(reviewMap.get("productId").toString()))
                                .build();
                    }).collect(Collectors.toList());
        }
        return (List<ReviewDTO>) reviews;
    }

    @Override
    public ResponseEntity<?> findReviewsByUser() {
        List<ReviewDTO> reviews=service.getAllReviewsByUser(userDetailService.getCurrentUser());
        List<EntityModel<ReviewDTO>> entityModelList=reviews.stream().map(r-> {
            EntityModel<ReviewDTO> reviewDTOEntityModel= EntityModel.of(r);
            reviewDTOEntityModel=HateoasUtils.convertEntityModelRelBase(reviewDTOEntityModel,IReviewController.class, r.getId().toString());
            return reviewDTOEntityModel;
        }).collect(Collectors.toList());
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .status(StatusEnum.OK)
                        .message("These are all reviews which user evaluate.")
                        .data(entityModelList)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getReviewsByProductId(Long productId) {
        List<ReviewDTO> reviews=service.getReviewByProductId(productId);
        List<EntityModel<ReviewDTO>> entityModelList=reviews.stream().map(r-> {
            EntityModel<ReviewDTO> reviewDTOEntityModel= EntityModel.of(r);
            reviewDTOEntityModel=HateoasUtils.convertEntityModelRelBase(reviewDTOEntityModel,IReviewController.class, r.getId().toString());
            return reviewDTOEntityModel;
        }).collect(Collectors.toList());
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .status(StatusEnum.OK)
                        .message("These are all reviews which this product evaluated")
                        .data(entityModelList)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> addReview(ReviewDTO dto) {
        var addedReview=service.addReviews(dto, userDetailService.getCurrentUser());
        return ResponseEntity.ok()
                .body(
                        SuccessResponse.builder()
                                .message("Added Review is successfully.")
                                .data(addedReview)
                                .status(StatusEnum.OK)
                                .build()
                );
    }

    @Override
    public ResponseEntity<?> updateReview(ReviewDTO dto, Long id) {
        var updatedReview=service.updateReviews(dto,id);
        return ResponseEntity.ok()
                .body(
                        SuccessResponse.builder()
                                .message("Updated Review is successfully.")
                                .data(updatedReview)
                                .status(StatusEnum.OK)
                                .build()
                );
    }

    @Override
    public ResponseEntity<?> deleteReviewById(Long id) throws AccessDeniedException {
        service.deleteReview(id, userDetailService.getCurrentUser());
        return ResponseEntity.ok()
                .body(
                        SuccessResponse.builder()
                                .message("Delete Review is successfully.")
                                .data(null)
                                .status(StatusEnum.OK)
                                .build()
                );
    }
}
