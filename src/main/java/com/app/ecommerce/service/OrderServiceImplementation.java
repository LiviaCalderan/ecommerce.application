package com.app.ecommerce.service;

import com.app.ecommerce.exceptions.APIException;
import com.app.ecommerce.exceptions.ResourceNotFoundException;
import com.app.ecommerce.model.*;
import com.app.ecommerce.payload.OrderDTO;
import com.app.ecommerce.payload.OrderItemDTO;
import com.app.ecommerce.payload.OrderRequestDTO;
import com.app.ecommerce.payload.OrderResponse;
import com.app.ecommerce.repository.*;
import com.app.ecommerce.repository.PaymentRepository;
import com.app.ecommerce.repository.ProductRepository;
import com.app.ecommerce.security.services.UserDetailsImplementation;
import com.app.ecommerce.util.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Override
    public OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Order> orderPage = orderRepository.findAll(pageDetails);
        List<Order> orderList = orderPage.getContent();

        if (orderList.isEmpty()) {
            throw new APIException("No order has been created yet");
        }
        List<OrderDTO> orderDTOS = orderList.stream()
                .map( order -> modelMapper.map(order, OrderDTO.class))
                .toList();

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setContent(orderDTOS);
        orderResponse.setPageNumber(orderPage.getNumber());
        orderResponse.setPageSize(orderPage.getSize());
        orderResponse.setTotalPages(orderPage.getTotalPages());
        orderResponse.setTotalElements(orderPage.getTotalElements());
        orderResponse.setLastPage(orderPage.isLast());
        return orderResponse;
    }

    @Override
    public OrderDTO updateOrderStatus(UUID orderId, OrderStatus orderStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId",orderId.toString()));
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public OrderResponse getAllSellerOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        User seller = authUtil.loggedInUser();

        Page<Order> orderPage = orderRepository.findAll(pageDetails);
        List<Order> sellerOrdersList = orderPage.getContent().stream()
                .filter(order -> order.getOrderItems().stream()
                        .anyMatch(orderItem -> {
                            var product = orderItem.getProduct();
                            if(product == null || product.getUser() == null) {
                                return false;
                            }
                            return product.getUser().getUserId().equals(seller.getUserId());
                        }))
                .toList();
        List<OrderDTO> orderDTOS = sellerOrdersList.stream()
                .map( order -> modelMapper.map(order, OrderDTO.class))
                .toList();

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setContent(orderDTOS);
        orderResponse.setPageNumber(orderPage.getNumber());
        orderResponse.setPageSize(orderPage.getSize());
        orderResponse.setTotalPages(orderPage.getTotalPages());
        orderResponse.setTotalElements(orderPage.getTotalElements());
        orderResponse.setLastPage(orderPage.isLast());
        return orderResponse;
    }

    @Override
    public OrderResponse getAllUserOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        User user = authUtil.loggedInUser();

        Page<Order> orderPage = orderRepository.findAll(pageDetails);
        List<Order> userOrdersList = orderPage.getContent().stream()
                .filter(order -> order.getEmail().equals(user.getEmail()))
                .toList();

        if (userOrdersList.isEmpty()) {
            throw new APIException("No order has been created yet");
        }
        List<OrderDTO> orderDTOS = userOrdersList.stream()
                .map( order -> modelMapper.map(order, OrderDTO.class))
                .toList();

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setContent(orderDTOS);
        orderResponse.setPageNumber(orderPage.getNumber());
        orderResponse.setPageSize(orderPage.getSize());
        orderResponse.setTotalPages(orderPage.getTotalPages());
        orderResponse.setTotalElements(orderPage.getTotalElements());
        orderResponse.setLastPage(orderPage.isLast());
        return orderResponse;
    }
}
