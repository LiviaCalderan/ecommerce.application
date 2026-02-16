package com.app.ecommerce.service;


import com.app.ecommerce.CategoryRepository;
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
        return categoryRepository.findAll();
    }

    @Override
    public Category createCategory() {

        return null;
    }

    @Override
    public Category deleteCategory(Long categoryId) {
        return null;
    }

    @Override
    public Category updateCategory(Long categoryId, Category updateCategory) {
        return null;
    }
}
