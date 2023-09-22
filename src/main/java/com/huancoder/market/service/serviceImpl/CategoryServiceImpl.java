package com.huancoder.market.service.serviceImpl;

import com.huancoder.market.dto.CategoryDto;
import com.huancoder.market.model.Category;
import com.huancoder.market.repository.CategoryRepository;
import com.huancoder.market.service.ICategoryService;
import com.huancoder.market.utils.CacheUtils;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    CategoryRepository repository;
    @Autowired
    ModelMapper mapper;
    @Autowired
    CacheUtils cacheUtils;
    @Override
    public List<CategoryDto> getAllCategory() {
        List<Category> categories=new ArrayList<>();
        categories = repository.findAll();
        log.info("Sized category is {}", categories.size());
        return categories.stream().map(category ->mapper.map(category, CategoryDto.class) ).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        if(cacheUtils.isExistCache("categories")){
            List<CategoryDto> categoryDtos=getAllCategory();
            CategoryDto categoryDtoResult=null;
            try {
                 categoryDtoResult = categoryDtos.stream().filter(c -> c.getId() == id).findFirst().orElseThrow();
            }catch (Exception e){
            }
            if(!Objects.isNull(categoryDtoResult)) {
                log.info("Get data from cache!");
                return categoryDtoResult;
            }

        }
        if(!repository.existsById(id))
            throw new NoSuchElementException("Category don't exist!");
        Category category=repository.findById(id).orElseThrow();
        return mapper.map(category, CategoryDto.class);
    }

    @Override
    public CategoryDto addCategory(CategoryDto newCategory) {
        Category category=mapper.map(newCategory, Category.class);
        category.setId(null);
        var addedCategory=repository.save(category);
        return mapper.map(category, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto updateCategory, Long id) {
        if(!repository.existsById(id))
            throw new NoSuchElementException("Category don't exist!");
        Category category=repository.findById(id).orElseThrow();
        category.setName(updateCategory.getName());
        category.setDescription(updateCategory.getDescription());
        category.setMainImg(updateCategory.getMainImg());
        category.setMainColor(updateCategory.getMainColor());
        var updatedCategory=repository.save(category);
        return mapper.map(category,CategoryDto.class);
    }

    @Override
    public void deleteCategory(Long id) {
        if(!repository.existsById(id))
            throw new NoSuchElementException("Category don't exist!");
        repository.deleteById(id);
    }
}
