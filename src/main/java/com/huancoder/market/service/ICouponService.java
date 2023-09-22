package com.huancoder.market.service;

import com.huancoder.market.dto.CouponDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ICouponService {
    @Cacheable("coupons")
    List<CouponDto> getAvailableCoupons();

    ResponseEntity<?> saveCoupon(long id);

    CouponDto addCoupon(CouponDto coupon);

    CouponDto getCouponDtoById(long id);


    CouponDto updateCoupon(long id, CouponDto request);

    List<CouponDto> findByProductId(Long productId);
}
