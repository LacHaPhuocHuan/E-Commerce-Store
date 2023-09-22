package com.huancoder.market.service;

import com.huancoder.market.dto.ProductDto;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface IProductService {
    ProductDto add(ProductDto addedProduct);

    @Cacheable("products")
    List<ProductDto> getAllProduct();

    ProductDto findById(Long id);

    ProductDto update(ProductDto updatedProduct, Long id);

    void deleteById(Long id);

    List<ProductDto> searchByKeyWord(String keyWord);


    List<ProductDto> getProductByOrderId(Long id);
}
