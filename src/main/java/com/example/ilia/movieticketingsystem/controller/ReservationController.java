package com.example.ilia.movieticketingsystem.controller;

import com.example.ilia.movieticketingsystem.DTO.ReservationDTO;
import com.example.ilia.movieticketingsystem.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @Operation(summary = "Reserve tickets for a specific session DO NOT SEND REQUESTS")
    @PostMapping("/reservation/{showtimeId}")
    public ResponseEntity<?> makeReservation (@RequestBody ReservationDTO reservationDTO, Authentication authentication){
        reservationService.makeReservation(reservationDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }






}
