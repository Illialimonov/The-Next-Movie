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
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long seatId;

    @ManyToOne
    @JoinColumn(name = "theater_id", referencedColumnName = "theater_id")
    private Theater theater;

    private Integer row_num;

    private Integer seat_num;

    @ManyToMany(mappedBy = "seatList")
    private List<Reservation> reservationList;
}
