package com.app.ecommerce.repository;

import com.app.ecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci FROM CartItem ci WHERE ci.product.id = ?1 AND ci.cart.id = ?2")
    CartItem findCartItemByProduct_ProductIdAndCart_CartId(Long productId, Long cartId);
}
