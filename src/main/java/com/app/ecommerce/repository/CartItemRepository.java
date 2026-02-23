package com.app.ecommerce.repository;

import com.app.ecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.product.id = ?2")
    CartItem findCartItemByProductIdAndCartId(Long productId, Long cartId);
}
