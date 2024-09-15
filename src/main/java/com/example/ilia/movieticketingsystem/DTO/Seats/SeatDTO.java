package com.example.ilia.movieticketingsystem.DTO.Seats;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class SeatDTO {
    private int seatId;
    private int row_num;
    private int seat_num;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SeatDTO seatDTO = (SeatDTO) obj;
        return seatId == seatDTO.seatId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seatId);
    }


}
