package com.example.ilia.movieticketingsystem.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class ReservationDTO {
    @JsonProperty("showtime_id")
    private int showtimeId;

    @JsonProperty("listOfSeats_id")
    private List<Integer> listOfSeatsId;

    @JsonProperty("buyer_email")
    private String buyerEmail;
}
