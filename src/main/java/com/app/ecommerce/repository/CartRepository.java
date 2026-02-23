package com.app.ecommerce.repository;

import com.app.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1")
    Cart findCartByEmail(String email);

    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1 AND c.cartId= ?2")
    Cart findCartByEmailAndCartId(String email, Long cartId);

    @Query("SELECT c FROM Cart c WHERE c JOIN FETCH c.cartItems ci JOIN FETCH ci.product p WHERE p.id = ?1")
    List<Cart> findCartsByProductId(Long productId);
}
