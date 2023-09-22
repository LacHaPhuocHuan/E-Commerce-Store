package com.huancoder.market.repository;

import com.huancoder.market.model.UserCoupon;
import com.huancoder.market.model.UserCouponKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface UserCouponRepository extends JpaRepository<UserCoupon, UserCouponKey> {
    int getIssuedById(@Param("couponId") long couponId,@Param("userId") Long userId);
}
