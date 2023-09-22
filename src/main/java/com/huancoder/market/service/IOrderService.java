package com.huancoder.market.service;

import com.huancoder.market.dto.OrderDto;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface IOrderService {
    @Cacheable("orders")
    public List<?> getAllOrder();

    OrderDto addOrder(OrderDto dto);

    OrderDto getOrderById(Long id);

    OrderDto updateOrder(OrderDto dto, Long id);

    void deletedOrderById(Long id);

    List<OrderDto> findByUserId(Long userId);
}
