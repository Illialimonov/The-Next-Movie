package com.example.ilia.movieticketingsystem.controller;

import com.example.ilia.movieticketingsystem.DTO.Response.AllSeatsPerShowtimesResponse;
import com.example.ilia.movieticketingsystem.DTO.Seats.AvailableSeatDTO;
import com.example.ilia.movieticketingsystem.DTO.Seats.SeatDTO;
import com.example.ilia.movieticketingsystem.service.SeatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/seats")
public class SeatController {
    private final SeatService seatService;

    @Operation(summary = "return all the seats by a showtime id")
    @GetMapping("/all/{showtimeId}")
    public List<SeatDTO> getSeats(@PathVariable int showtimeId) {
        return seatService.getAllSeats(showtimeId);
    }

    @Operation(summary = "return all the RESERVED seats by a showtime id")
    @GetMapping("/reserved/{showtimeId}")
    public List<SeatDTO> getReserved(@PathVariable int showtimeId) {
        return seatService.getReservedSeats(showtimeId);
    }

    @Operation(summary = "return all RESERVED and NOT RESERVED seats by showtime ID (see example)")
    @GetMapping("/{showtimeId}")
    public List<AvailableSeatDTO> getAvailable(@PathVariable int showtimeId) {
        return seatService.getAvailableSeats(showtimeId);
    }

    @Operation(summary = "Returns all RESERVED and NOT RESERVED seats for ALL FUTURE showtimes")
    @GetMapping("/all")
    public List<AllSeatsPerShowtimesResponse> getFutureSeatsAll() {
        return seatService.getFutureShowtimeAvailableSeats();
    }


}
