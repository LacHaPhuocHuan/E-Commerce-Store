package com.huancoder.market.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbCoupon")
@NamedQuery(name = "Coupon.getMaxCouponCount", query = "Select c.maxCouponCount from Coupon c where c.id= :id")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private Long minPrice;

    private Long discountPrice;

    private int maxCouponCount;

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime expirationTime;
    @ManyToMany(mappedBy = "coupons")
    private List<Product> product;
}
