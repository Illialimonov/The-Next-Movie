package com.example.ilia.movieticketingsystem.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.ChargeCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

@Service
public class StripeService {
    public Charge createCharge(String token, long amount) throws StripeException {
        ChargeCreateParams params= ChargeCreateParams.builder()
                .setAmount(amount)
                .setCurrency("usd")
                .setSource(token)
                .setDescription("Example charge")
                .build();

        return Charge.create(params);
    }

    public PaymentIntent createPaymentIntent(long amount) throws StripeException {
        PaymentIntentCreateParams paymentIntentCreateParams = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency("usd")
                .build();

        return PaymentIntent.create(paymentIntentCreateParams);
    }
}
