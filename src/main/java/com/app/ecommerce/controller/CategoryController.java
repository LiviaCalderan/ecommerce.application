package com.app.ecommerce.controller;

import com.app.ecommerce.model.Category;
import com.app.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {

    @Autowired
    public final CategoryService categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<List<Category> getAllCategories(){
        return new ResponseEntity<>(categoryService.fetchCategories(), HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<Category> createCategory(@RequestBody Category category){
        Category createdCategory = categoryService.createCategory();
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("admin/categories/{categoryId}")
    public ResponseEntity<Category> deleteCategory(@PathVariable Long categoryId){
        Category deletedCategory = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(deletedCategory, HttpStatus.OK);
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long categoryId,
                                                   @RequestBody Category updateCategory){
        Category updatedCategory = categoryService.updateCategory(categoryId, updateCategory);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }


}
