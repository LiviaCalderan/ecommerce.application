package com.app.ecommerce.service;

import com.app.ecommerce.exceptions.APIException;
import com.app.ecommerce.exceptions.ResourceNotFoundException;
import com.app.ecommerce.model.Cart;
import com.app.ecommerce.model.CartItem;
import com.app.ecommerce.model.Product;
import com.app.ecommerce.payload.CartDTO;
import com.app.ecommerce.payload.ProductDTO;
import com.app.ecommerce.repository.CartItemRepository;
import com.app.ecommerce.repository.CartRepository;
import com.app.ecommerce.repository.ProductRepository;
import com.app.ecommerce.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CartServiceImplementation implements CartService {


    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    private final AuthUtil authUtil;
    private final ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {

        Cart cart = checkUserCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProduct_ProductIdAndCart_CartId(productId, cart.getCartId());

        if(cartItem != null) {
            throw new APIException("Product " + product.getProductName() + " already exists");
        }
        if(product.getQuantity() == 0) {
            throw new APIException("Product " + product.getProductName() + " is not available");
        }
        if(product.getQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getProductName()
                    + " less than or equal to " + product.getQuantity());
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());
        cartItemRepository.save(newCartItem);

        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productDTOStream = cartItems.stream().map(item -> {
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });

        cartDTO.setProductDTOS(productDTOStream.toList());

        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> cartsList = cartRepository.findAll();

        if(cartsList.isEmpty()) {
            throw new APIException("No carts exists");
        }
        List<CartDTO> cartDTOSList = cartsList.stream()
                .map(cart -> {
                    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
                    List<ProductDTO> products = mapperToProductDTO(cart);
                    cartDTO.setProductDTOS(products);
                    return cartDTO;
                }).toList();
        return cartDTOSList;

    }

    @Override
    public CartDTO getCart() {
        String email = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(email);
        Long cartId = cart.getCartId();

        Cart existingCart = cartRepository.findCartByEmailAndCartId(email, cartId);
        if(existingCart == null) {
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }
        CartDTO cartDTO = modelMapper.map(existingCart, CartDTO.class);
        List<ProductDTO> products = mapperToProductDTO(existingCart);
        cartDTO.setProductDTOS(products);

        double total = cart.getCartItems().stream()
                .mapToDouble(item -> item.getProductPrice() * item.getQuantity())
                .sum();

        cartDTO.setTotalPrice(total);

        return cartDTO;
    }

    private Cart checkUserCart(){

        Cart userCart = cartRepository.findCartByEmail((authUtil.loggedInEmail()));
        if(userCart != null){
            return userCart;
        }

        Cart cart = new Cart();
        cart.setUser(authUtil.loggedInUser());
        cart.setTotalPrice(0.00);
        return cartRepository.save(cart);
    }

    private List<ProductDTO> mapperToProductDTO(Cart cart) {
        List<ProductDTO> productDTOS = cart.getCartItems().stream()
                .map(item -> {
                    ProductDTO dto = modelMapper.map(item.getProduct(), ProductDTO.class);
                    dto.setQuantity(item.getQuantity());
                    dto.setDiscount(item.getDiscount());
                    return dto;
                })
                .toList();
        return productDTOS;
    }

}
