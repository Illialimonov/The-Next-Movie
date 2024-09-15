package com.example.ilia.movieticketingsystem.DTO.Response.checkoutComponents;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieComponentResponse {
    private String title;
    private String description;
    private String director;
    private Integer duration;
    private String posterURL;
}
