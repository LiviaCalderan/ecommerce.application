package com.app.ecommerce.service;

import com.app.ecommerce.model.Category;

import java.util.List;

public interface CategoryService {
    
    List<Category> fetchCategories();

    void createCategory();

    Category deleteCategory(Long categoryId);


    Category updateCategory(Long categoryId, Category updateCategory);
}
