package com.app.ecommerce.service;


import com.app.ecommerce.CategoryRepository;
import com.app.ecommerce.exceptions.APIException;
import com.app.ecommerce.exceptions.ResourceNotFoundException;
import com.app.ecommerce.model.Category;
import com.app.ecommerce.payload.CategoryDTO;
import com.app.ecommerce.payload.CategoryResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImplementation implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponseDTO fetchCategories() {
        List<Category> listCategories = categoryRepository.findAll();
        if (listCategories.isEmpty())
            throw new APIException("No category has been created yet");
        List<CategoryDTO> categoryDTOS = listCategories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        CategoryResponseDTO categoryResponse = new CategoryResponseDTO();
        categoryResponse.setContent(categoryDTOS);
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category categoryFromDB = categoryRepository.findByCategoryName(category.getCategoryName());
        if(categoryFromDB != null)
            throw new APIException("Category " + category.getCategoryName() + " already exist!");
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    /* TODO Implement DTO in the Delete Method */


    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category categoryToDelete = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        categoryRepository.delete(categoryToDelete);
        return modelMapper.map(categoryToDelete, CategoryDTO.class);

    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO updateCategoryDTO) {
        Category existingCategoryDB = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        existingCategoryDB.setCategoryName(updateCategoryDTO.getCategoryName());
        Category updatedCategory = categoryRepository.save(existingCategoryDB);
        return modelMapper.map(updatedCategory, CategoryDTO.class);
    }
}
