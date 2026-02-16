package com.app.ecommerce.controller;

import com.app.ecommerce.model.Category;
import com.app.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {

    public final CategoryService categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<List<Category>> getAllCategories(){
        return new ResponseEntity<>(categoryService.fetchCategories(), HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category){
        Category createdCategory = categoryService.createCategory(category);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId){
        Category deletedCategory = categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long categoryId,
                                                   @Valid @RequestBody Category updateCategory){
        Category updatedCategory = categoryService.updateCategory(categoryId, updateCategory);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }


}
