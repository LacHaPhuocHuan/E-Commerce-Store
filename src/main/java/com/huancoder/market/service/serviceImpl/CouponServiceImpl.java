package com.huancoder.market.service.serviceImpl;

import com.huancoder.market.dto.CouponDto;
import com.huancoder.market.dto.common.StatusEnum;
import com.huancoder.market.dto.common.SuccessResponse;
import com.huancoder.market.model.Coupon;
import com.huancoder.market.model.UserCoupon;
import com.huancoder.market.model.UserCouponKey;
import com.huancoder.market.repository.CouponRepository;
import com.huancoder.market.repository.UserCouponRepository;
import com.huancoder.market.security.MyUserDetailService;
import com.huancoder.market.service.ICouponService;
import com.huancoder.market.utils.CacheUtils;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CouponServiceImpl implements ICouponService {
    @Autowired
    private CouponRepository repository;
    @Autowired
    private UserCouponRepository userCouponRepository;
    @Autowired
    MyUserDetailService userDetailService;
    @Autowired
    ModelMapper mapper;
    @Autowired
    CacheUtils cacheUtils;
    @Override
    public List<CouponDto> getAvailableCoupons() {
        List<Coupon> coupons;
        try {
            coupons = repository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            coupons = new ArrayList<>();
        }
        List<CouponDto> couponDtos = coupons.stream()
                .filter(c -> c.getExpirationTime().isAfter(ZonedDateTime.now()))
                .map(c -> mapper.map(c, CouponDto.class))
                .collect(Collectors.toList());

        return couponDtos;

    }

    @Override
    public ResponseEntity<?> saveCoupon(long id) {
        if(!checkIsAvailableCouponById(id))
            throw new IllegalArgumentException ("Coupon don't exist or unavailable!");
        var maxCouponCount=repository.getMaxCouponCount(id);
        var issuedCount=0;
        var user=userDetailService.getCurrentUser();
        if(checkIsUserCouponHave(id,user.getId()))
            issuedCount=userCouponRepository.getIssuedById(id,user.getId() );
        if(maxCouponCount==issuedCount)
            throw new IllegalArgumentException ("The maximum number of coupons has been reached");
        if(!checkIsUserCouponHave(id,user.getId())){
            var userCoupon= UserCoupon.builder()
                    .key(UserCouponKey.builder()
                            .userId(user.getId())
                            .couponId(id)
                            .build())
                    .user(user)
                    .coupon(repository.findById(id).orElseThrow())
                    .issuedCount(issuedCount)
                    .build();
            userCouponRepository.save(userCoupon);
        }else{
            var userCoupon=userCouponRepository.findById(
                    UserCouponKey.builder()
                            .userId(user.getId())
                            .couponId(id).
                            build())
                    .orElseThrow();
            userCoupon.setIssuedCount(issuedCount+1);
            userCouponRepository.save(userCoupon);

        }
        return ResponseEntity.ok(SuccessResponse.builder()
                .message("Saved Success!")
                .status(StatusEnum.OK).build()
        );
    }

    private boolean checkIsUserCouponHave(long couponId, Long userId) {
        return userCouponRepository.existsById(
                UserCouponKey.builder()
                        .couponId(couponId)
                        .userId(userId)
                        .build());
    }

    private boolean checkIsAvailableCouponById(long id) {
        var coupon=repository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("Coupon don't exist !"));
        return coupon.getExpirationTime().isAfter(ZonedDateTime.now());
    }

    @Override
    public CouponDto addCoupon(CouponDto request) {
        Coupon coupon= mapper.map(request, Coupon.class);
        coupon.setId(null);
        var couponSave=repository.save(coupon);
        return mapper.map(couponSave,CouponDto.class);
    }

    @Override
    public CouponDto getCouponDtoById(long id) {
        var coupon=repository.findById(id);
        return mapper.map(coupon,CouponDto.class);
    }

    @Override
    public CouponDto updateCoupon(long id, CouponDto request) {
        Coupon coupon=repository.findById(id).orElseThrow();
        coupon.setMaxCouponCount(request.getMaxCouponCount());
        coupon.setName(request.getName());
        coupon.setMinPrice(request.getMinPrice());
        coupon.setDiscountPrice(request.getDiscountPrice());
        coupon.setExpirationTime(request.getExpirationTime());
        var couponUpdated=repository.save(coupon);
        return mapper.map(couponUpdated, CouponDto.class);
    }

    @Override
    public List<CouponDto> findByProductId(Long productId) {
        List<Coupon> coupons=repository.findByProduct_Id(productId);
        return coupons.stream()
                .map(c->mapper.map(c, CouponDto.class)).collect(Collectors.toList());
    }


}
