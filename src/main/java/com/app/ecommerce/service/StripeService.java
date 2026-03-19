package com.app.ecommerce.service;

import com.app.ecommerce.payload.StripePaymentDTO;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.stereotype.Service;

@Service
public interface StripeService {

    PaymentIntent paymentIntent(StripePaymentDTO stripePaymentDTO) throws StripeException;
}
