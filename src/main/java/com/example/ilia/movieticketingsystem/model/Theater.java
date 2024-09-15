package com.example.ilia.movieticketingsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name="theater")
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_id") // This column will be named "movie_id"
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

    @OneToMany(mappedBy = "theater", fetch = FetchType.EAGER)
    private List<Showtime> showtimeList;

    private int lowerBoundarySeatId;

    private int upperBoundarySeatId;

    private int pricePerSeat;




    @OneToMany(mappedBy = "theater", fetch = FetchType.EAGER)
    private List<Seat> seats;
}
