package com.huancoder.market.controller;

import com.huancoder.market.dto.ProductDto;
import jakarta.websocket.server.PathParam;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Parameter;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/products")
public interface IProductController {
    @GetMapping("")
    public ResponseEntity<?> getProducts();
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") final Long id);
    @PostMapping("")
    @CacheEvict(value = "products", allEntries = true)
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> add(@RequestBody ProductDto addedProduct);

    @PutMapping("/{id}")
    @CacheEvict(value = "products", allEntries = true)
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> update(@RequestBody ProductDto updatedProduct,@PathVariable("id") final Long id);

    @DeleteMapping("/{id}")
    @CacheEvict(value = "products", allEntries = true)
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> deleteById(@PathVariable("id") final Long id);

    @GetMapping("/search")
    public ResponseEntity<?> findByKeyWord(@PathParam("keyWord") String keyWord);


    //--------------------QUAN HE -----------------------------

    @GetMapping("/o/{order_id}")
    public ResponseEntity<?> getProductByOrderId(@PathVariable("order_id") final Long id);

}
