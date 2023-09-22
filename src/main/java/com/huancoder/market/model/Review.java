package com.huancoder.market.model;

import com.huancoder.market.dto.Evaluation;
import com.huancoder.market.model.Product;
import com.huancoder.market.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbReview")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String content;
    @Enumerated(EnumType.STRING)
    private Evaluation evaluation;
    private int score;
    private LocalDateTime createdAt;
    private List<String> imgs;

}
