package com.app.ecommerce.service;

import com.app.ecommerce.model.Product;
import com.app.ecommerce.payload.ProductDTO;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, Product product);
}
