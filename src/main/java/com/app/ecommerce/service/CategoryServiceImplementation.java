package com.app.ecommerce.service;


import com.app.ecommerce.CategoryRepository;
import com.app.ecommerce.exceptions.APIException;
import com.app.ecommerce.exceptions.ResourceNotFoundException;
import com.app.ecommerce.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImplementation implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> fetchCategories() {
        List<Category> list = categoryRepository.findAll();
        if (list.isEmpty())
            throw new APIException("No category has been created yet");
        return list;
    }

    @Override
    public Category createCategory(Category category) {
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory != null)
            throw new APIException("Category " + category.getCategoryName() + " already exist!");
        return categoryRepository.save(category);
    }

    @Override
    public Category deleteCategory(Long categoryId) {
        Category categoryToDelete = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        categoryRepository.delete(categoryToDelete);
        return categoryToDelete;
    }

    @Override
    public Category updateCategory(Long categoryId, Category updateCategory) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        existingCategory.setCategoryName(updateCategory.getCategoryName());
        return categoryRepository.save(existingCategory);
    }
}
