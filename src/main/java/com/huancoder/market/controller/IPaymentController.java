package com.huancoder.market.controller;

import com.huancoder.market.dto.PaymentDTO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RequestMapping("/api/v1/payments")
public interface IPaymentController {
    @GetMapping("")
    public ResponseEntity<?> getAllPayments();
    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable("id")final Long id);
    @PostMapping("")
    @CacheEvict(value = "payments", allEntries = true)
    public ResponseEntity<?> addPayment(@RequestBody PaymentDTO dto);

    @PutMapping("/{id}")
    @CacheEvict(value = "payments", allEntries = true)
    public ResponseEntity<?> updatePayment(@RequestBody PaymentDTO dto, @PathVariable("") final Long id);
    @DeleteMapping("/{id}")
    @CacheEvict(value = "payments", allEntries = true)
    public ResponseEntity<?> deletePaymentById(@PathVariable("") final Long id);

    //------------------QUAN HE------------------------------------



}
