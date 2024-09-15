package com.example.ilia.movieticketingsystem.DTO.Response.checkoutComponents;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class SeatComponentResponse {
    private List<HashMap<String, Integer>> seats;
}
