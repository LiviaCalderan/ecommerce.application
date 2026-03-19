package com.app.ecommerce.service;

import com.app.ecommerce.payload.StripePaymentDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class StripeServiceImplementation implements StripeService {

    @Value("${STRIPE_SECRET_KEY}")
    private String stripeAPIKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeAPIKey;
    }

    @Override
    public PaymentIntent paymentIntent(StripePaymentDTO stripePaymentDTO) throws StripeException {

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(stripePaymentDTO.getAmount())
                        .setCurrency(stripePaymentDTO.getCurrency())
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .build();
        return PaymentIntent.create(params);
    }
}
