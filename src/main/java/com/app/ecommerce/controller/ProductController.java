package com.app.ecommerce.controller;

import com.app.ecommerce.config.AppConstants;
import com.app.ecommerce.payload.ProductDTO;
import com.app.ecommerce.payload.ProductResponseDTO;
import com.app.ecommerce.repository.ProductRepository;
import com.app.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Product related operations")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    @Operation(
            summary = "Add Product To Category",
            description = "Creates a new product and links it to the informed category."
    )
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDto,
                                                 @PathVariable Long categoryId){

        ProductDTO savedproductDTO = productService.addProduct(categoryId, productDto);
        return new ResponseEntity<>(savedproductDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/product")
    @Operation(
            summary = "Get All Products",
            description = "Returns a paginated list of products with sorting options."
    )
    public ResponseEntity<ProductResponseDTO> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        ProductResponseDTO productResponse = productService.fetchAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>( productResponse, HttpStatus.OK);

    }

    @GetMapping("/public/categories/{categoryId}/products")
    @Operation(
            summary = "Get Products By Category",
            description = "Returns paginated products filtered by the informed category."
    )
    public ResponseEntity<ProductResponseDTO> getProductsByCategory(@PathVariable Long categoryId,
                                                                    @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                    @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                    @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                    @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){

        ProductResponseDTO productsByCategory = productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>( productsByCategory, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    @Operation(
            summary = "Get Products By Keyword",
            description = "Searches products by keyword with pagination and sorting."
    )
    public ResponseEntity<ProductResponseDTO> getProductsByKeyword(@PathVariable String keyword,
                                                                   @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                   @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                   @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                   @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
        ProductResponseDTO productsByKeyword = productService.searchByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productsByKeyword, HttpStatus.FOUND);
    }

    @PutMapping("/admin/products/{productId}")
    @Operation(
            summary = "Update Product",
            description = "Updates the product data for the informed product id."
    )
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId,
                                                    @Valid @RequestBody ProductDTO updateProductDTO){
        ProductDTO updatedProductDTO = productService.updateProduct(productId, updateProductDTO);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    @Operation(
            summary = "Delete Product",
            description = "Removes the product identified by the informed id."
    )
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        ProductDTO productDTO = productService.deleteProduct(productId);
        return new ResponseEntity<>(productDTO, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/products/{productId}/image")
    @Operation(
            summary = "Update Product Image",
            description = "Uploads and replaces the image of the informed product."
    )
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("image") MultipartFile image) throws IOException {
        ProductDTO updateProductDTO = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(updateProductDTO, HttpStatus.OK);

    }

}
