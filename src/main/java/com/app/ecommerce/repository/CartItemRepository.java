package com.app.ecommerce.repository;

import com.app.ecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci FROM CartItem ci WHERE ci.product.productId = ?1 AND ci.cart.cartId = ?2")
    CartItem findCartItemByProduct_ProductIdAndCart_CartId(Long productId, Long cartId);
}
