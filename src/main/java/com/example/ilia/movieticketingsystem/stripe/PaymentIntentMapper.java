package com.example.ilia.movieticketingsystem.stripe;

import com.stripe.model.PaymentIntent;

public class PaymentIntentMapper {

    public static PaymentIntentDTO toDTO(PaymentIntent paymentIntent) {
        PaymentIntentDTO dto = new PaymentIntentDTO();
        dto.setId(paymentIntent.getId());
        dto.setStatus(paymentIntent.getStatus());
        dto.setAmount(paymentIntent.getAmount());
        // Map other fields if needed
        return dto;
    }
}
