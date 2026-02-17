package com.app.ecommerce.controller;

import com.app.ecommerce.model.Product;
import com.app.ecommerce.payload.ProductDTO;
import com.app.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody Product product, @PathVariable Long categoryId){

        ProductDTO productDTO = productService.addProduct(categoryId, product);
        return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
    }


}
