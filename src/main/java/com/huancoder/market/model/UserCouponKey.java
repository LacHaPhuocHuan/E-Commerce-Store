package com.huancoder.market.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserCouponKey {
    @Column(name = "user_id")
    Long userId;

    @Column(name = "coupon_id")
    Long couponId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCouponKey that = (UserCouponKey) o;
        return Objects.equals(userId, that.userId) && Objects.equals(couponId, that.couponId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, couponId);
    }
}
