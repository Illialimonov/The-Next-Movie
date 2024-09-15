package com.example.ilia.movieticketingsystem.stripe;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentIntentDTO {
    private String id;
    private String status;
    private long amount;
    private String currency;
    private String description;
    private long created;
    private String paymentMethod;
    // Add other relevant fields

    // Constructors, getters, and setters
}
