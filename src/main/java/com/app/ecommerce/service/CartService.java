package com.app.ecommerce.service;

import com.app.ecommerce.payload.CartDTO;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);
}
