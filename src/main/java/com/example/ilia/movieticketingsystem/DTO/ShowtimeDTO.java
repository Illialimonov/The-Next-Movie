package com.example.ilia.movieticketingsystem.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ShowtimeDTO {

    @JsonProperty("movie_id")
    private Integer movieId;
    @JsonProperty("theater_id")
    private Integer theaterId;
    private LocalDateTime start;
}
