package com.huancoder.market.controller;

import com.huancoder.market.dto.CategoryDto;
import com.huancoder.market.security.Role;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/api/v1/categories", consumes = {"application/json","application/*+json"})
public interface ICategoryController {
    @GetMapping("")
    public ResponseEntity<?> getCategories();
    //Them day du cac link

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") final Long id);
    //day du links


    @PostMapping("")
    @CacheEvict(value ="categories", allEntries = true)
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDto newCategory);

    @PutMapping("/{id}")
    @CacheEvict(value ="categories", allEntries = true)
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDto updateCategory, @PathVariable("id") final long id);

    @DeleteMapping("/{id}")
    @CacheEvict(value ="categories", allEntries = true)
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") final Long id);

}
