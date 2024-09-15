package com.example.ilia.movieticketingsystem.DTO.Response;

import com.example.ilia.movieticketingsystem.model.Movie;
import com.example.ilia.movieticketingsystem.model.Theater;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ShowtimeResponse {
    private Long showtimeId;

    private int movieId;

    private String movieName;

    private int theaterId;

    private String theaterName;

    private String screenType;

    private LocalDateTime startingDate;

    private LocalDateTime endingDate;

    private Integer pricePerSeat;
}
