package com.example.ilia.movieticketingsystem.DTO.Response.checkoutComponents;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowtimeComponentResponse {
    private int showtimeId;
    private String startingDate;
    private String theater;
}
