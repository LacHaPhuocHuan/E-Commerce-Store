package com.huancoder.market.controller.controllerImpl;

import com.huancoder.market.controller.ICategoryController;
import com.huancoder.market.controller.ICouponController;
import com.huancoder.market.controller.IProductController;
import com.huancoder.market.dto.ProductDto;
import com.huancoder.market.dto.common.StatusEnum;
import com.huancoder.market.dto.common.SuccessResponse;
import com.huancoder.market.service.IProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Log4j2
public class ProductControllerImpl implements IProductController {
    @Autowired
    IProductService productService;
    @Override
    public ResponseEntity<?> getProducts() {
        List<ProductDto> products=getAllProduct();
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .message("These are all the valid product!")
                        .status(StatusEnum.OK)
                        .data(convertToHateoas(products)).build()
        );
    }

    private List<ProductDto> getAllProduct() {
        List<?> products=productService.getAllProduct();
        if(products.stream().allMatch(p->p instanceof LinkedHashMap)){
            products=products.stream().map(product->{
                LinkedHashMap<String, Object> productMap=(LinkedHashMap<String, Object>) product;
                return ProductDto.builder()
                        .name(((LinkedHashMap) product).get("name").toString())
                        .mainImg(productMap.get("mainImg").toString())
                        .price(Long.parseLong(productMap.get("price").toString()))
                        .description(productMap.get("description").toString())
                        .id(Long.parseLong(productMap.get("id").toString()))
                        .categoryId(Long.parseLong(productMap.get("categoryId").toString()))
                        .detailsImg(productMap.get("detailsImg").toString()).build();
            }).collect(Collectors.toList());
        }
        return (List<ProductDto>) products;
    }

    @Override
    public ResponseEntity<?> getProductById(Long id) {
        var product=productService.findById(id);
        EntityModel<ProductDto> entityModel =  EntityModel.of(product);
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(IProductController.class).getProducts()).withSelfRel());
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(IProductController.class).deleteById(product.getId())).withRel("delete"));
        entityModel.add(WebMvcLinkBuilder.linkTo(IProductController.class).slash(product.getId()).withRel("update"));
        entityModel.add(WebMvcLinkBuilder.linkTo(IProductController.class).withRel("add"));
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ICouponController.class).getCouponsByProductId(product.getId())).withRel("coupons for product"));
        entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ICategoryController.class).getCategoryById(product.getId())).withRel("category"));
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .message("This is product that was found!")
                        .data(entityModel)
                        .status(StatusEnum.OK).build()

        );
    }

    @Override
    public ResponseEntity<?> add(ProductDto addedProduct) {
        var product=productService.add(addedProduct);
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .message("added product is successfully")
                        .data(product)
                        .status(StatusEnum.OK)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> update(ProductDto updatedProduct, Long id) {
        var resultedUpdate=productService.update(updatedProduct, id);
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .message("Updating product is successfully!")
                        .data(resultedUpdate)
                        .status(StatusEnum.OK)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> deleteById(Long id) {
         productService.deleteById(id);
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .status(StatusEnum.OK)
                        .message("Deleted product is successfully")
                        .build()
        );
    }


    @Override
    public ResponseEntity<?> findByKeyWord(String keyWord) {
        List<ProductDto> productDtos=productService.searchByKeyWord(keyWord);

        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .message("These are all the valid product!")
                        .status(StatusEnum.OK)
                        .data(convertToHateoas(productDtos)).build()
        );
    }


    List<EntityModel<ProductDto>> convertToHateoas(List<ProductDto> productDtos){
        List<EntityModel<ProductDto>> productEntityModels=productDtos.stream()
                .map(p-> {
                    log.info("ID: {}", p.getId());
                    EntityModel<ProductDto> entityModel =  EntityModel.of(p);
                    entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(IProductController.class).getProductById(p.getId())).withSelfRel());
                    entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(IProductController.class).deleteById(p.getId())).withRel("delete"));
                    entityModel.add(WebMvcLinkBuilder.linkTo(IProductController.class).slash(p.getId()).withRel("update"));
                    entityModel.add(WebMvcLinkBuilder.linkTo(IProductController.class).withRel("add"));
                    entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ICouponController.class).getCouponsByProductId(p.getId())).withRel("coupons for product"));
                    entityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ICategoryController.class).getCategoryById(p.getId())).withRel("category"));
                    return entityModel;
                }).collect(Collectors.toList());
        return productEntityModels;
    }

    @Override
    public ResponseEntity<?> getProductByOrderId(Long id) {
        List<ProductDto> productDTOs = productService.getProductByOrderId(id);
        var entityModelProductList=convertToHateoas(productDTOs);
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .message("These are all the valid product!")
                        .status(StatusEnum.OK)
                        .data(entityModelProductList).build()
        );
    }
}
