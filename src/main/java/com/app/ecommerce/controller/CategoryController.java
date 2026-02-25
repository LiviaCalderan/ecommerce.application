package com.app.ecommerce.controller;

import com.app.ecommerce.config.AppConstants;
import com.app.ecommerce.payload.CategoryDTO;
import com.app.ecommerce.payload.CategoryResponseDTO;
import com.app.ecommerce.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Category", description = "Category related operations")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/public/categories")
    @Operation(
            summary = "Get All Categories",
            description = "Returns a paginated list of categories with sorting options."
    )
    public ResponseEntity<CategoryResponseDTO> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        CategoryResponseDTO categoryResponseDTO = categoryService.fetchCategories(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(categoryResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    @Operation(
            summary = "Create New Category",
            description = "Creates a new category with the provided information."
    )
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    @Operation(
            summary = "Delete Category",
            description = "Deletes the category identified by the informed id."
    )
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId){
        CategoryDTO deletedCategory = categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/public/categories/{categoryId}")
    @Operation(
            summary = "Update Category",
            description = "Updates the category data for the informed id."
    )
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId,
                                                      @Valid @RequestBody CategoryDTO updateCategoryDTO){
        CategoryDTO updatedCategoryDTO = categoryService.updateCategory(categoryId, updateCategoryDTO);
        return new ResponseEntity<>(updatedCategoryDTO, HttpStatus.OK);
    }


}
