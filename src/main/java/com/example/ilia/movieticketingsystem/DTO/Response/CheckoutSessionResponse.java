package com.example.ilia.movieticketingsystem.DTO.Response;

import com.example.ilia.movieticketingsystem.DTO.Response.checkoutComponents.MovieComponentResponse;
import com.example.ilia.movieticketingsystem.DTO.Response.checkoutComponents.PricingComponentResponse;
import com.example.ilia.movieticketingsystem.DTO.Response.checkoutComponents.SeatComponentResponse;
import com.example.ilia.movieticketingsystem.DTO.Response.checkoutComponents.ShowtimeComponentResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutSessionResponse {
    private MovieComponentResponse movieComponentResponse;
    private ShowtimeComponentResponse showtimeComponentResponse;
    private SeatComponentResponse seatComponentResponse;
    private PricingComponentResponse pricingComponentResponse;

    private String checkoutStripeSession;
}
