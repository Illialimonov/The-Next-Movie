package com.example.ilia.movieticketingsystem.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserOrderResponse {
    @JsonProperty("showtime_id")
    private int showtimeId;

    @JsonProperty("listOfSeats_id")
    private List<Integer> listOfSeatsId;

    @JsonProperty("buyer_email")
    private String buyerEmail;

    @JsonProperty("final_cost")
    private String finalCost;
}
