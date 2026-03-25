package com.app.ecommerce.controller;

import com.app.ecommerce.config.AppConstants;
import com.app.ecommerce.payload.*;
import com.app.ecommerce.security.services.UserDetailsImplementation;
import com.app.ecommerce.service.OrderService;
import com.app.ecommerce.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Order related operations")
public class OrderController {

    private final OrderService orderService;

    private final StripeService stripeService;

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

    @PostMapping("/order/stripe-client-secret")
    @Operation(
            summary = "Create Stripe Client Secret",
            description = "Create Stripe Client Secret"
    )
    public ResponseEntity<String> createStripeClientSecret(@RequestBody StripePaymentDTO stripePaymentDTO) throws StripeException {
        System.out.println("StripePaymentDTO: " + stripePaymentDTO);
        PaymentIntent paymentIntent = stripeService.paymentIntent(stripePaymentDTO);
        return new ResponseEntity<String>(paymentIntent.getClientSecret(), HttpStatus.CREATED);
    }

    @GetMapping("/admin/orders")
    @Operation(
            summary = "Get All Orders",
            description = "Fetch orders to display on Admin Panel."
    )
    public ResponseEntity<OrderResponse> getAllOrders(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ORDERS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder){
        OrderResponse orderResponse = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortOrder);
        return  new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    @GetMapping("/seller/orders")
    @Operation(
            summary = "Get All Orders by Seller",
            description = "Fetch orders to display on Seller Panel."
    )
    public ResponseEntity<OrderResponse> getAllSellerOrders(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ORDERS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder){
        OrderResponse orderResponse = orderService.getAllSellerOrders(pageNumber, pageSize, sortBy, sortOrder);
        return  new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    @GetMapping("/user/orders")
    @Operation(
            summary = "Get All Orders by User",
            description = "Fetch orders to display on User Profile."
    )
    public ResponseEntity<OrderResponse> getAllUserOrders(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ORDERS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder){
        OrderResponse orderResponse = orderService.getAllUserOrders(pageNumber, pageSize, sortBy, sortOrder);
        return  new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    @PutMapping("/seller/orders/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable UUID orderId,
                                                      @RequestBody OrderStatusUpdateDTO orderStatusUpdateDTO) {

        OrderDTO order = orderService.updateOrderStatus(orderId, orderStatusUpdateDTO.getOrderStatus());
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
