package com.app.ecommerce.controller;

import com.app.ecommerce.model.Cart;
import com.app.ecommerce.payload.CartDTO;
import com.app.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Cart related operations")
public class CartController {

    private final CartService cartService;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    @Operation(
            summary = "Add Product To Cart",
            description = "Adds the informed product to the current user cart with the given quantity."
    )
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
                                                    @PathVariable Integer quantity){
        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @GetMapping("/carts/users/cart")
    @Operation(
            summary = "Get Current User Cart",
            description = "Returns the current authenticated user cart details."
    )
    public ResponseEntity<CartDTO> getUserCart(){
        CartDTO cartDTO = cartService.getCart();
        return new ResponseEntity<>(cartDTO, HttpStatus.FOUND);
    }

    // ------- for admin panels --------
    @GetMapping("/carts")
    @Operation(
            summary = "Get All Carts",
            description = "Returns all carts, intended for administrative usage."
    )
    public ResponseEntity<List<CartDTO>> getAllCarts(){
        List<CartDTO> cartDTOS = cartService.getAllCarts();
        return new ResponseEntity<List<CartDTO>>(cartDTOS, HttpStatus.FOUND);
    }

    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    @Operation(
            summary = "Update Product Quantity In Cart",
            description = "Increases or decreases product quantity in cart based on operation."
    )
    public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId,
                                                     @PathVariable String operation){
        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId, operation.equalsIgnoreCase("delete") ? -1 : 1);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @DeleteMapping("/cart/product/{productId}")
    @Operation(
            summary = "Delete Product From Cart",
            description = "Removes the informed product from the current user cart."
    )
    public ResponseEntity<CartDTO> deleteCartProduct(@PathVariable Long productId){
        CartDTO cartDTO = cartService.deleteCartItem(productId);
        return new ResponseEntity<>(cartDTO, HttpStatus.NO_CONTENT);
    }
}
