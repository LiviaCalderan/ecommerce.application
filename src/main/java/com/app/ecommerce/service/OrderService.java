package com.app.ecommerce.service;

import com.app.ecommerce.model.OrderStatus;
import com.app.ecommerce.payload.OrderDTO;
import com.app.ecommerce.payload.OrderRequestDTO;
import com.app.ecommerce.payload.OrderResponse;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface OrderService {

    @Transactional
    OrderDTO placeOrder(OrderRequestDTO orderRequestDTO, String paymentMethod);

    OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);


    OrderDTO updateOrderStatus(UUID orderId, OrderStatus orderStatus);
}
