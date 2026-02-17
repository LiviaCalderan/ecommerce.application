package com.app.ecommerce.service;

import com.app.ecommerce.payload.ProductDTO;
import com.app.ecommerce.payload.ProductResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO product);

    ProductResponseDTO fetchAllProducts();

    ProductResponseDTO searchByCategory(Long categoryId);

    ProductResponseDTO searchByKeyword(String keyword);

    ProductDTO updateProduct(Long productId, ProductDTO updateProductDTO);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
