package com.example.ilia.movieticketingsystem.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewEmailDTO {
    @Email
    @NotBlank(message = "The email of the listing is mandatory")
    @NotNull
    private String newEmail;
}
