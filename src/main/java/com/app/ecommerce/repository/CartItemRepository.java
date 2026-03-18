package com.app.ecommerce.repository;

import com.app.ecommerce.model.Cart;
import com.app.ecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci FROM CartItem ci WHERE ci.product.productId = ?1 AND ci.cart.cartId = ?2")
    CartItem findCartItemByProduct_ProductIdAndCart_CartId(Long productId, Long cartId);

    @Query("DELETE FROM CartItem ci WHERE ci.cart.cartId = ?1")
    List<Cart> deleteAllByCartId(Long cartId);
}
