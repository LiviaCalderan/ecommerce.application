package com.app.ecommerce.service;

import com.app.ecommerce.payload.OrderDTO;
import com.app.ecommerce.payload.OrderRequestDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {

    @Transactional
    OrderDTO placeOrder(OrderRequestDTO orderRequestDTO, String paymentMethod);
}
