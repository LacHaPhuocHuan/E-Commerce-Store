package com.huancoder.market.controller;

import com.huancoder.market.dto.OrderDto;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/orders")
public interface IOrderController {
    @GetMapping("")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> getOrders();
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable("id") final Long id);

    @PostMapping("")
    @CacheEvict(value = "orders",allEntries = true)
    public ResponseEntity<?> addOrder(@RequestBody OrderDto dto);

    @PutMapping("/{id}")
    @CacheEvict(value = "orders",allEntries = true)
    public ResponseEntity<?> updateOrder(@RequestBody OrderDto dto,@PathVariable("id") final Long id );
    @DeleteMapping("{id}")
    @CacheEvict(value = "orders",allEntries = true)
    public  ResponseEntity<?> deleteOrder(@PathVariable("id") final Long id);



    //-------------------QUAN HE-----------------------------

    @GetMapping("/u/{user_id}")
    public ResponseEntity<?> getOrdersByUserId(@PathVariable("user_id") final Long userId);
    @GetMapping("/u")
    public ResponseEntity<?> getOrderByUser();
}
