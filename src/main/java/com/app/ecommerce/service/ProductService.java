package com.app.ecommerce.service;

import com.app.ecommerce.model.Product;
import com.app.ecommerce.payload.ProductDTO;
import com.app.ecommerce.payload.ProductResponseDTO;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, Product product);

    ProductResponseDTO fetchAllProducts();

    ProductResponseDTO searchByCategory(Long categoryId);

    ProductResponseDTO searchByKeyword(String keyword);

    ProductDTO updateProduct(Long productId, ProductDTO updateProductDTO);
}
