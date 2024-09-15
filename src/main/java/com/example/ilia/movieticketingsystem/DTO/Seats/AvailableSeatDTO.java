package com.example.ilia.movieticketingsystem.DTO.Seats;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvailableSeatDTO {
    private int seatId;
    private int row_num;
    private int seat_num;
    private Boolean availability;

}
