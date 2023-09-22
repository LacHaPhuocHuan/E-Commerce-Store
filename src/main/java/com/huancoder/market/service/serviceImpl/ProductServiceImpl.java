package com.huancoder.market.service.serviceImpl;

import com.huancoder.market.dto.ProductDto;
import com.huancoder.market.model.Category;
import com.huancoder.market.model.OrderProduct;
import com.huancoder.market.model.Product;
import com.huancoder.market.repository.CategoryRepository;
import com.huancoder.market.repository.OrderProductRepository;
import com.huancoder.market.repository.ProductRepository;
import com.huancoder.market.service.IProductService;
import com.huancoder.market.utils.CacheUtils;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ProductServiceImpl implements IProductService {
    @Autowired
    ProductRepository repository;
    @Autowired
    ModelMapper mapper;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CacheUtils cacheUtils;
    @Autowired
    OrderProductRepository orderProductRepository;
    @Override
    public ProductDto add(ProductDto addedProduct) {

        Product product=mapper.map(addedProduct, Product.class);
        Category category=categoryRepository.findById(addedProduct.getCategoryId()).orElseThrow(()->new NoSuchElementException("Category don' exist!"));
        product.setCategory(category);
        product.setId(null);
        log.info(product.toString());
        Product addedResultProduct=repository.save(product);
        return mapper.map(addedResultProduct, ProductDto.class);
    }

    @Override
    public List<ProductDto> getAllProduct() {
        List<Product> products=new ArrayList<>();
        try {
            products = repository.findAll();
        }catch (Exception e){
        }
        return products.stream().map(p->mapper.map(p, ProductDto.class)).collect(Collectors.toList());
    }

    @Override
    public ProductDto findById(Long id) {
        if(cacheUtils.isExistCache("products")){
            List<ProductDto> productDtos=getAllProduct();
            ProductDto product01= null;
            try {
                product01=productDtos.stream()
                        .filter(p -> p.getId() == id).findFirst().orElseThrow();
                return product01;
            }catch (Exception e){
            }
        }
        if(!repository.existsById(id)){
            throw  new NoSuchElementException("Product don't exist!");
        }
        cacheUtils.clearMyCache("products");
        Product product=repository.findById(id).orElseThrow();
        return mapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto updated, Long id) {
        if(!repository.existsById(id)){
            throw  new NoSuchElementException("Product don't exist!");
        }
        if(!categoryRepository.existsById(updated.getCategoryId()))
            throw  new NoSuchElementException("Category don't exist!");
        Product product=repository.findById(id).orElseThrow();
        product.setName(updated.getName());
        product.setDescription(updated.getDescription());
        product.setPrice(updated.getPrice());
        product.setMainImg(updated.getMainImg());
        product.setDetailsImg(updated.getDetailsImg());
        product.setCategory(categoryRepository.findById(updated.getCategoryId()).orElseThrow());
        var updatedProduct=repository.save(product);
        return mapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public void deleteById(Long id) {
        if(!repository.existsById(id)){
            throw  new NoSuchElementException("Product don't exist!");
        }
        repository.deleteById(id);
    }

    @Override
    public List<ProductDto> searchByKeyWord(String keyWord) {
        if(cacheUtils.isExistCache("products")){
            List<ProductDto> productDtos=getAllProduct();
            log.info("Using cache!");
            try {
                productDtos= productDtos.stream()
                        .filter(c -> {
                            String name = c.getName().toLowerCase();
                            String description = c.getDescription().toLowerCase();
                            String keyword = keyWord.trim().toLowerCase();
                            return name.contains(keyword) || description.contains(keyword);
                        }).collect(Collectors.toList());
                if(productDtos.size()>0) return productDtos;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        log.info("Using jpa!");
        List<Product> products=repository.findByNameContainingOrDescriptionContaining(keyWord, keyWord);
        return products.stream().map(p->mapper.map(p, ProductDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProductByOrderId(Long id) {
        List<OrderProduct> orderProducts=orderProductRepository.findByOrderId(id);
        List<ProductDto> products = orderProducts.stream()
                .map(op -> mapper.map(op.getProduct(), ProductDto.class))
                .collect(Collectors.toList());

        products.forEach(p -> p.setQuantity(
                orderProducts.stream()
                        .filter(op -> op.getProduct().getId().equals(p.getId()))
                        .findFirst()
                        .map(OrderProduct::getQuantity)
                        .orElse(0)
        ));
        return products;
    }


}
