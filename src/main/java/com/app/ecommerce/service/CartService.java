package com.app.ecommerce.service;

import com.app.ecommerce.payload.CartDTO;
import com.app.ecommerce.payload.CartItemsDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCart();

    @Transactional
    CartDTO updateProductQuantityInCart(Long productId, Integer quantity);

    CartDTO deleteCartItem(Long productId);

    void updateProductInCarts(Long cartId, Long productId);

    @Transactional
    void deleteCartItemByCartId(Long cartId, Long productId);

}
