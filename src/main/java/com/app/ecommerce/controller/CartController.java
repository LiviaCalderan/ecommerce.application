package com.app.ecommerce.controller;

import com.app.ecommerce.model.Cart;
import com.app.ecommerce.payload.CartDTO;
import com.app.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
                                                    @PathVariable Integer quantity){
        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getUserCart(){
        CartDTO cartDTO = cartService.getCart();
        return new ResponseEntity<>(cartDTO, HttpStatus.FOUND);
    }

    // ------- for admin panels --------
    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getAllCarts(){
        List<CartDTO> cartDTOS = cartService.getAllCarts();
        return new ResponseEntity<List<CartDTO>>(cartDTOS, HttpStatus.FOUND);
    }

    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId,
                                                     @PathVariable String operation){
        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId, operation.equalsIgnoreCase("delete") ? -1 : 1);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }
}
