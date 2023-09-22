package com.huancoder.market.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huancoder.market.model.Product;
import com.huancoder.market.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private Long userId;
    private Long productId;
    private String content;
    private Evaluation evaluation;
    private int score;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    private List<String> imgs;
}
