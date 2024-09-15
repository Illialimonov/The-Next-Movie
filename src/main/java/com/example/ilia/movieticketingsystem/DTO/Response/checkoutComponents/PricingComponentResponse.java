package com.example.ilia.movieticketingsystem.DTO.Response.checkoutComponents;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PricingComponentResponse {
    private int pricePerTicket;
    private Long subtotal;
    private int discounts;
    private Double taxes;
    private Double totalAmount;
}
