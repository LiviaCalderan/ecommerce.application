package com.app.ecommerce.service;

import com.app.ecommerce.payload.ProductDTO;
import com.app.ecommerce.payload.ProductResponseDTO;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO product);

    ProductResponseDTO fetchAllProducts();

    ProductResponseDTO searchByCategory(Long categoryId);

    ProductResponseDTO searchByKeyword(String keyword);

    ProductDTO updateProduct(Long productId, ProductDTO updateProductDTO);

    ProductDTO deleteProduct(Long productId);
}
