package com.example.ilia.movieticketingsystem.controller;

import com.example.ilia.movieticketingsystem.DTO.TheaterDTO;
import com.example.ilia.movieticketingsystem.service.TheaterService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/theaters")
public class TheaterController {
    private final TheaterService theaterService;

    @Operation(summary = "return all theaters")
    @GetMapping("/all")
    public List<TheaterDTO> getAllTheaters() {
        return theaterService.getAllTheaters();
    }


}