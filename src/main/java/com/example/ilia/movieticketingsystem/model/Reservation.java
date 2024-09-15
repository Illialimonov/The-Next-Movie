package com.example.ilia.movieticketingsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id") // This column will be named "movie_id"
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "showtime_id", referencedColumnName = "showtime_id")
    private Showtime showtime;

    @ManyToMany
    @JoinTable(
            name = "reservation_seat", // Join table name
            joinColumns = @JoinColumn(name = "reservation_id"), // Column in join table for Reservation
            inverseJoinColumns = @JoinColumn(name = "seat_id") // Column in join table for Seat
    )
    private List<Seat> seatList;


    private String buyerEmail;



}
