package com.example.ilia.movieticketingsystem.controller;


import com.example.ilia.movieticketingsystem.DTO.GoogleLoginRequest;
import com.example.ilia.movieticketingsystem.auth.*;
import com.example.ilia.movieticketingsystem.config.JwtService;
import com.example.ilia.movieticketingsystem.model.RefreshToken;
import com.example.ilia.movieticketingsystem.model.User;
import com.example.ilia.movieticketingsystem.service.RefreshTokenService;
import com.example.ilia.movieticketingsystem.util.AuthError;
import com.example.ilia.movieticketingsystem.util.AuthExceptionResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;


@RestController
@RequestMapping("/api/auth")

@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Account successfully created");
    }


    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody GoogleLoginRequest loginRequest) throws Exception {
        return service.loginWithGoogle(loginRequest);
    }

    @GetMapping("/currentusername")
    public String currentUserName(Authentication authentication) {
        return authentication.getName();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }


    @PostMapping("/refreshtoken")
    public AuthenticationResponse refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return service.refreshToken(refreshTokenRequestDTO);
    }


    @ExceptionHandler
    private ResponseEntity<AuthExceptionResponse> handleException(AuthError e) {
        AuthExceptionResponse response = new AuthExceptionResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
