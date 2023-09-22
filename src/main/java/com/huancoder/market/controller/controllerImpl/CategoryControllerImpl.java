package com.huancoder.market.controller.controllerImpl;

import com.huancoder.market.controller.ICategoryController;
import com.huancoder.market.dto.CategoryDto;
import com.huancoder.market.dto.common.StatusEnum;
import com.huancoder.market.dto.common.SuccessResponse;
import com.huancoder.market.service.ICategoryService;
import com.huancoder.market.utils.CacheUtils;
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
public class CategoryControllerImpl implements ICategoryController {
    @Autowired
    ICategoryService categoryService;
    @Autowired
    CacheUtils cacheUtils;
    @Override
    public ResponseEntity<?> getCategories() {
        List<CategoryDto> categoryDTOs=getAllCategory();
        List<EntityModel<CategoryDto>> entityModelCateDTOs=categoryDTOs.stream()
                .map(ca-> {
                    EntityModel<CategoryDto> categoryDtoEntityModel=  EntityModel.of(ca);
                    categoryDtoEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ICategoryController.class).getCategoryById(ca.getId())).withSelfRel());
                    categoryDtoEntityModel.add(WebMvcLinkBuilder.linkTo(ICategoryController.class).slash("").withRel("add"));
                    categoryDtoEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ICategoryController.class).deleteCategory(ca.getId())).withRel("delete"));
                    categoryDtoEntityModel.add(WebMvcLinkBuilder.linkTo(ICategoryController.class).slash(ca.getId()).withRel("update"));
                    return categoryDtoEntityModel;
                }).collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(SuccessResponse.builder()
                        .message("These are all categories!")
                        .data(entityModelCateDTOs)
                        .status(StatusEnum.OK).build());
    }

    private List<CategoryDto> getAllCategory() {
        List<?> categoryDtos= categoryService.getAllCategory();
        log.info("Sized category is {}", categoryDtos.size());
        if (categoryDtos.stream().allMatch(c -> c instanceof LinkedHashMap)) {
            categoryDtos=categoryDtos.stream()
                    .map(c->{
                        LinkedHashMap<String, Object> cateMap=(LinkedHashMap<String, Object>) c;
                        return CategoryDto.builder()
                                .id(Long.parseLong(((LinkedHashMap) c).get("id").toString()))
                                .name(((LinkedHashMap) c).get("name").toString())
                                .description(((LinkedHashMap) c).get("description").toString())
                                .mainColor(((LinkedHashMap) c).get("mainColor").toString())
                                .mainImg(((LinkedHashMap) c).get("mainImg").toString())
                                .build();
                    }).collect(Collectors.toList());
        }
        return (List<CategoryDto>) categoryDtos;
    }

    @Override
    public ResponseEntity<?> getCategoryById(Long id) {
        CategoryDto categoryDto=categoryService.getCategoryById(id);
        EntityModel<CategoryDto> categoryDtoEntityModel=  EntityModel.of(categoryDto);
        categoryDtoEntityModel
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ICategoryController.class).getCategories()).withSelfRel());
        categoryDtoEntityModel
                .add(WebMvcLinkBuilder.linkTo(ICategoryController.class).slash("").withRel("add"));
        categoryDtoEntityModel
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ICategoryController.class).deleteCategory(categoryDto.getId())).withRel("delete"));
        categoryDtoEntityModel
                .add(WebMvcLinkBuilder.linkTo(ICategoryController.class).slash(categoryDto.getId()).withRel("update"));

        return ResponseEntity.ok()
                .body(SuccessResponse.builder()
                        .message("This is a category which id is"+id+"!")
                        .data(categoryDtoEntityModel)
                        .status(StatusEnum.OK).build());
    }

    @Override
    public ResponseEntity<?> addCategory(CategoryDto newCategory) {
        var addedCategory=categoryService.addCategory(newCategory);
        return ResponseEntity.ok()
                .body(SuccessResponse.builder()
                        .message("Added category is successfully")
                        .data(addedCategory)
                        .status(StatusEnum.OK).build());
    }

    @Override
    public ResponseEntity<?> updateCategory(CategoryDto updateCategory, long id) {
        var updatedCategory=categoryService.updateCategory(updateCategory,id);
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .status(StatusEnum.OK)
                        .message("Updated category is successfully!").build()
        );
    }

    @Override
    public ResponseEntity<?> deleteCategory(Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().body(
                SuccessResponse.builder()
                        .message("Deleted Category is successfully!")
                        .status(StatusEnum.OK)
                        .build()
        );
    }
}
