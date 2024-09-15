package com.example.ilia.movieticketingsystem.DTO.Response;

import com.example.ilia.movieticketingsystem.DTO.Seats.AvailableSeatDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AllSeatsPerShowtimesResponse {
    private int movieId;
    private int showtimeId;
    private int theaterId;
    private List<AvailableSeatDTO> AllSeatsPerShowtime;
}
