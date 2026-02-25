package com.app.ecommerce.controller;

import com.app.ecommerce.payload.OrderDTO;
import com.app.ecommerce.payload.OrderRequestDTO;
import com.app.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order/user/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderRequestDTO orderRequestDTO,
                                                @PathVariable String paymentMethod) {
        OrderDTO order = orderService.placeOrder(orderRequestDTO, paymentMethod);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}
