package com.example.ilia.movieticketingsystem.DTO.Seats;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.stream.Collectors;

import java.util.List;

@Setter
@Getter
public class SeatsResponseDTO {
    private List<AvailableSeatDTO> availableSeats;
    private List<AvailableSeatDTO> reservedSeats;

    // Constructors, getters, and setters
    public SeatsResponseDTO(List<SeatDTO> availableSeats, List<SeatDTO> reservedSeats, int showtimeId) {
        this.availableSeats = availableSeats.stream()
                .map(seat -> convertToAvailableSeatDTO(seat, true, showtimeId))
                .collect(Collectors.toList());
        this.reservedSeats = reservedSeats.stream()
                .map(seat -> convertToAvailableSeatDTO(seat, false, showtimeId))
                .collect(Collectors.toList());
    }

    private AvailableSeatDTO convertToAvailableSeatDTO(SeatDTO seatDTO, boolean b, int showtimeId) {
        AvailableSeatDTO availableSeatDTO = new AvailableSeatDTO();
        availableSeatDTO.setRow_num(seatDTO.getRow_num());
        availableSeatDTO.setSeat_num(seatDTO.getSeat_num());
        availableSeatDTO.setAvailability(b);
        availableSeatDTO.setSeatId(seatDTO.getSeatId());
        return availableSeatDTO;
    }

    public List<AvailableSeatDTO> getAllSeats() {
        List<AvailableSeatDTO> allSeats = new ArrayList<>(availableSeats);
        allSeats.addAll(reservedSeats);
        return allSeats;
    }

}

