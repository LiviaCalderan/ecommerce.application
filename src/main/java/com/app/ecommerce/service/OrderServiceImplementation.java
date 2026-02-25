package com.app.ecommerce.service;

import com.app.ecommerce.exceptions.APIException;
import com.app.ecommerce.exceptions.ResourceNotFoundException;
import com.app.ecommerce.model.*;
import com.app.ecommerce.payload.OrderDTO;
import com.app.ecommerce.payload.OrderItemDTO;
import com.app.ecommerce.payload.OrderRequestDTO;
import com.app.ecommerce.repository.*;
import com.app.ecommerce.repository.PaymentRepository;
import com.app.ecommerce.repository.ProductRepository;
import com.app.ecommerce.util.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImplementation implements OrderService {

    private final OrderRepository orderRepository;
    private final AuthUtil authUtil;
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final PaymentRepository paymentRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final CartService cartService;

    @Override
    @Transactional
    public OrderDTO placeOrder(OrderRequestDTO orderRequestDTO, String paymentMethod) {

        String email = authUtil.loggedInEmail();

        Cart userCart = cartRepository.findCartByEmail(email);
        if (userCart == null) {
            throw new ResourceNotFoundException("Cart",  "email", email);
        }
        List<CartItem> cartItemList = userCart.getCartItems();
        if (cartItemList.isEmpty()) {
            throw new APIException("Cart is empty");
        }

        Address address = addressRepository.findById(orderRequestDTO.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address",  "id", orderRequestDTO.getAddressId()));

        Order order = new Order();
        order.setEmail(email);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(userCart.getTotalPrice());
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setAddress(address);

        String pgPaymentId = orderRequestDTO.getPgPaymentId();
        String pgStatus = orderRequestDTO.getPgStatus();
        String pgResponseMessage = orderRequestDTO.getPgResponseMessage();
        String pgName =orderRequestDTO.getPgName();
        Payment payment = new Payment(paymentMethod, pgPaymentId, pgStatus, pgResponseMessage, pgName);
        payment.setOrder(order);
        paymentRepository.save(payment);
        order.setPayment(payment);

        Order savedOrder = orderRepository.save(order);


        List<OrderItem> orderItemList = new ArrayList<>();
        for(CartItem cartItem : cartItemList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(savedOrder);
            orderItemList.add(orderItem);
        }
        orderItemList = orderItemRepository.saveAll(orderItemList);

        List<CartItem> cartItemsCopy = new ArrayList<>(userCart.getCartItems());

        cartItemsCopy.forEach(item -> {
            int quantity = item.getQuantity();
            Product product = item.getProduct();
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);
        });

        cartItemsCopy.forEach(item -> {
            cartService.deleteCartItem(item.getProduct().getProductId());
        });

        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        orderItemList.forEach(item ->
                orderDTO.getOrderItems()
                        .add(modelMapper.map(item, OrderItemDTO.class)));
        orderDTO.setAddressId(orderRequestDTO.getAddressId());
        return orderDTO;

    }
}
