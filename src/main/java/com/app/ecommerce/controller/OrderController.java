package com.app.ecommerce.controller;

import com.app.ecommerce.payload.OrderDTO;
import com.app.ecommerce.payload.OrderRequestDTO;
import com.app.ecommerce.payload.StripePaymentDTO;
import com.app.ecommerce.service.OrderService;
import com.app.ecommerce.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
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
}
