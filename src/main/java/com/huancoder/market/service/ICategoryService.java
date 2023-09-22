package com.huancoder.market.service;

import com.huancoder.market.dto.CategoryDto;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface ICategoryService {
    @Cacheable("categories")
    List<CategoryDto> getAllCategory();

    CategoryDto getCategoryById(Long id);

    CategoryDto addCategory(CategoryDto newCategory);

    CategoryDto updateCategory(CategoryDto updateCategory, Long id);

    void deleteCategory(Long id);
}
