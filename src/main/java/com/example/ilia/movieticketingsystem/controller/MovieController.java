package com.example.ilia.movieticketingsystem.controller;

import com.example.ilia.movieticketingsystem.DTO.MovieCreateDTO;
import com.example.ilia.movieticketingsystem.DTO.MovieResponse;
import com.example.ilia.movieticketingsystem.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;

    @Operation(summary = "return all movies")
    @GetMapping("/all")
    public List<MovieResponse> getAllUpcomingMovies() {
        return movieService.getAllMovies();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "create a movie")
    @PostMapping("/create")
    public ResponseEntity<?> createMovie(@RequestPart("file") MultipartFile file, @RequestPart("movie_data") String listingData) {
        return movieService.createMovie(file,listingData);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable("id") int id){
        return movieService.deleteMovie(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMovie(@RequestBody MovieCreateDTO movieCreateDTO, @PathVariable("id") int id){
        return movieService.updateMovie(movieCreateDTO, id);
    }




}
