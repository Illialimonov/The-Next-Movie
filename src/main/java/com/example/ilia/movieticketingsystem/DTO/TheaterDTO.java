package com.example.ilia.movieticketingsystem.DTO;

import com.example.ilia.movieticketingsystem.model.Showtime;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TheaterDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_id")
    private Integer theaterId;

    @NotNull(message = "The name cannot be null!")
    @Size(min=1, max=100, message = "The name should be from 1 to 100 characters!")
    private String name;

    @Min(value = 20, message = "capacity must be at least 20 seats.")
    @Max(value = 100, message = "capacity cannot exceed 100 seats.")
    private Integer capacity;

    @NotNull(message = "The name screenType be null!")
    @Size(min=1, max=100, message = "The screenType should be from 1 to 100 characters!")
    private String screenType;
    

    private int lowerBoundarySeatId;

    private int upperBoundarySeatId;
}
