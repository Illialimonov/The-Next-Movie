package com.example.ilia.movieticketingsystem.controller;

import com.example.ilia.movieticketingsystem.DTO.ReservationDTO;
import com.example.ilia.movieticketingsystem.DTO.Response.CheckoutSessionResponse;
import com.example.ilia.movieticketingsystem.DTO.Response.checkoutComponents.MovieComponentResponse;
import com.example.ilia.movieticketingsystem.model.Movie;
import com.example.ilia.movieticketingsystem.repository.ShowtimeRepository;
import com.example.ilia.movieticketingsystem.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class CheckoutController {

    private final PaymentService paymentService;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Autowired
    public CheckoutController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = "open a session to make a payment")
    @PostMapping("/create-checkout-session")
    public ResponseEntity<CheckoutSessionResponse> createCheckoutSession(@RequestBody ReservationDTO reservationDTO, Authentication authentication) {
        String customizedStripeLink = paymentService.generateStripeLink(reservationDTO, authentication);
        CheckoutSessionResponse checkoutSessionResponse = paymentService.composeCheckoutResponse(reservationDTO);
        checkoutSessionResponse.setCheckoutStripeSession(customizedStripeLink);

        return ResponseEntity.ok(checkoutSessionResponse);
    }

    @Operation(summary = "DO NOT SEND REQUESTS it is used to process a successful Stripe operation")
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        return paymentService.handleStripePayment(payload, sigHeader);
    }

}
