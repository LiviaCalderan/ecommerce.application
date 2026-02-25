package com.app.ecommerce.controller;

import com.app.ecommerce.payload.OrderDTO;
import com.app.ecommerce.payload.OrderRequestDTO;
import com.app.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Order related operations")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order/user/payments/{paymentMethod}")
    @Operation(
            summary = "Create Order",
            description = "Creates an order for the authenticated user using the selected payment method."
    )
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderRequestDTO orderRequestDTO,
                                                @PathVariable String paymentMethod) {
        OrderDTO order = orderService.placeOrder(orderRequestDTO, paymentMethod);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}
