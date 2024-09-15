package com.example.ilia.movieticketingsystem.controller;

import com.example.ilia.movieticketingsystem.DTO.Response.ShowtimeResponse;
import com.example.ilia.movieticketingsystem.DTO.ShowtimeDTO;
import com.example.ilia.movieticketingsystem.service.ShowtimeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/showtime")
public class ShowtimeController {
    private final ShowtimeService showtimeService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "create a showtime (admin panel)")
    @PostMapping("/create")
    public ResponseEntity<?> createShowtime(@RequestBody ShowtimeDTO showtimeDTO) throws Exception {
        showtimeService.saveShowtimeDTO(showtimeDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "view all showtimes")
    @GetMapping("/all")
    public List<ShowtimeResponse> seeReservation(){
        return showtimeService.allShowtime();
    }

    @Operation(summary = "view future showtimes")
    @GetMapping("/future")
    public List<ShowtimeResponse> seeFutureReservation(){
        return showtimeService.futureShowtime();
    }
}
