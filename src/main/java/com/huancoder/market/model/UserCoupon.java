package com.huancoder.market.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbUserCoupon")
@NamedQuery(name = "UserCoupon.getIssuedById", query ="select uc.issuedCount from UserCoupon uc where uc.key.userId=:userId and uc.key.couponId=:couponId " )
public class UserCoupon {
    @EmbeddedId
    UserCouponKey key;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("couponId")
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    private int useCount;

    private int issuedCount;
}
