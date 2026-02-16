package com.app.ecommerce.service;

import com.app.ecommerce.model.Category;

import java.util.List;

public interface CategoryService {
    
    List<Category> fetchCategories();

    Category createCategory(Category category);

    Category deleteCategory(Long categoryId);

    Category updateCategory(Long categoryId, Category updateCategory);
}
