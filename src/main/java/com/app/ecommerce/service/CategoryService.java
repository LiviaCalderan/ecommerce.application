package com.app.ecommerce.service;

import com.app.ecommerce.model.Category;
import com.app.ecommerce.payload.CategoryDTO;
import com.app.ecommerce.payload.CategoryResponseDTO;

public interface CategoryService {
    
    CategoryResponseDTO fetchCategories();

    CategoryDTO createCategory(CategoryDTO category);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(Long categoryId, CategoryDTO updateCategoryDTO);
}
