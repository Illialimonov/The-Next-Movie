package com.example.ilia.movieticketingsystem.service;

import com.example.ilia.movieticketingsystem.DTO.MovieCreateDTO;
import com.example.ilia.movieticketingsystem.DTO.MovieResponse;
import com.example.ilia.movieticketingsystem.configS3.StorageService;
import com.example.ilia.movieticketingsystem.model.Movie;
import com.example.ilia.movieticketingsystem.model.User;
import com.example.ilia.movieticketingsystem.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final ModelMapper modelMapper;
    private final StorageService storageService;

    public List<MovieResponse> getAllMovies() {
        return movieRepository.findAll().stream().sorted(Comparator.comparing(m -> m.getMovieStatus().toUpperCase().charAt(0))).map(this::convertToMovieDTO)
                .collect(Collectors.toList());
    }

    public MovieResponse convertToMovieDTO(Movie movie){
        return modelMapper.map(movie, MovieResponse.class);
    }

    public ResponseEntity<?> createMovie(MultipartFile file, String movieData) {
        MovieCreateDTO movieCreateDTO;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule for LocalDate
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Ensure dates are not serialized as timestamps
            movieCreateDTO = objectMapper.readValue(movieData, MovieCreateDTO.class);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid movie data");
        }

        // Convert DTO to entity and add listing
        Movie movieToAdd = convertToMovie(movieCreateDTO);
        addMovie(movieToAdd, file);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private void addMovie(Movie movieToAdd, MultipartFile file) {

        movieToAdd.setPosterURL(storageService.uploadFile(file));
        movieRepository.save(movieToAdd);
    }

    private Movie convertToMovie(MovieCreateDTO movieCreateDTO) {
        Movie movie = modelMapper.map(movieCreateDTO, Movie.class);
        movie.setMovieStatus("NOW_GOING");
        return movie;
    }

    public ResponseEntity<?> deleteMovie(int id) {
        movieRepository.deleteById(id);
        return ResponseEntity.ok("movie was deleted");
    }

    public ResponseEntity<?> updateMovie(MovieCreateDTO movieCreateDTO, int id) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if (optionalMovie.isPresent()) {
            Movie existingMovie = optionalMovie.get();
            modelMapper.map(movieCreateDTO, existingMovie);
            existingMovie.setPosterURL(movieRepository.findById(id).orElseThrow().getPosterURL());
            return ResponseEntity.ok(movieRepository.save(existingMovie));}

        throw new RuntimeException("Movie not found with id " + id);
    }
}
