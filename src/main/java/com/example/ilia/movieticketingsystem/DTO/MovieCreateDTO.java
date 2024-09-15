package com.example.ilia.movieticketingsystem.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MovieCreateDTO {

    @NotNull(message = "The title cannot be null!")
    @Size(min=1, max=1000, message = "The title should be from 1 to 1000 characters!") // This column will be named "title"
    private String title;

    @NotNull(message = "The description cannot be null!")
    @Size(min=1, max=1000, message = "The description should be from 1 to 1000 characters!")
    private String description;

    @NotNull(message = "The director's name cannot be null!")
    @Size(min=1, max=100, message = "The director's name should be from 1 to 100 characters!")
    private String director;

    @Size(min=1, max=100, message = "The category name should be from 1 to 100 characters!")
    private String category;

    @NotNull(message = "The duration cannot be null!")
    @Min(value = 20, message = "Duration must be at least 20 minutes.")
    @Max(value = 500, message = "Duration cannot exceed 500 minutes.")
    private Integer duration;

    @JsonProperty("release_date")
    @NotNull(message = "The releaseDate cannot be null!")
    private LocalDate releaseDate;

    @JsonProperty("trailer_url")
    @NotNull(message = "The trailerURL cannot be null!")
    private String trailerURL;
}
