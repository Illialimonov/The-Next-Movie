package com.example.ilia.movieticketingsystem.service;

import com.example.ilia.movieticketingsystem.DTO.Response.ShowtimeResponse;
import com.example.ilia.movieticketingsystem.DTO.ShowtimeDTO;
import com.example.ilia.movieticketingsystem.model.Showtime;
import com.example.ilia.movieticketingsystem.repository.MovieRepository;
import com.example.ilia.movieticketingsystem.repository.ShowtimeRepository;
import com.example.ilia.movieticketingsystem.repository.TheaterRepository;
import com.example.ilia.movieticketingsystem.util.ShowtimeOperationException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Service
@AllArgsConstructor
public class ShowtimeService {
    private final ShowtimeRepository showtimeRepository;
    private final ModelMapper modelMapper;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void saveShowtimeDTO(ShowtimeDTO showtimeDTO) throws Exception {

        Showtime showtime = new Showtime();
        showtime.setStartingDate(showtimeDTO.getStart());
        showtime.setMovie(movieRepository.findById(showtimeDTO.getMovieId()).orElseThrow());
        showtime.setTheater(theaterRepository.findById(showtimeDTO.getTheaterId()).orElseThrow());

        showtime.setEndingDate(showtimeDTO.getStart()
                .plus(Duration.ofMinutes(movieRepository.findById(showtimeDTO.getMovieId())
                        .orElseThrow().getDuration())));
        showtime.setAvailableSeats(theaterRepository.findById(showtimeDTO.getTheaterId()).orElseThrow().getCapacity());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime showtimeStart = LocalDateTime.parse(showtimeDTO.getStart().toString());

       if(now.isBefore(showtimeStart)
               && isTheatreAvailable(showtimeDTO)
       ){
           showtimeRepository.save(showtime);
       } else {
           throw new ShowtimeOperationException("The movie can't start earlier than right now");
       }
    }

    private boolean isTheatreAvailable(ShowtimeDTO showtimeDTO) {
        List<ShowtimeResponse> futureShowtimes = futureShowtime();
        for (ShowtimeResponse showtimeResponse : futureShowtimes){
            if (checkAvailability(showtimeResponse, showtimeDTO)){
                if(showtimeResponse.getTheaterId()==showtimeDTO.getTheaterId()){
                    throw new ShowtimeOperationException("The theater is busy at current time!");
                }
            }
        }
        return true;
    }


    private boolean checkAvailability(ShowtimeResponse showtimeResponse, ShowtimeDTO showtimeDTO) {
        LocalDateTime start = showtimeDTO.getStart();
        LocalDateTime end = showtimeDTO.getStart().plusMinutes(movieRepository.findById(showtimeDTO.getMovieId()).orElseThrow().getDuration());


        boolean checkIfStartInBetween = (showtimeResponse.getStartingDate().isBefore(start) || (showtimeResponse.getStartingDate().isEqual(start))) && showtimeResponse.getEndingDate().isAfter(start);
        boolean checkIfEndInBetween = (showtimeResponse.getStartingDate().isBefore(end) || (showtimeResponse.getStartingDate().isEqual(end))) && (showtimeResponse.getEndingDate().isAfter(end));

        return checkIfStartInBetween||checkIfEndInBetween;
    }

    public List<ShowtimeResponse> futureShowtime() {
        return showtimeRepository.findAll().stream()
                .filter(showtime -> showtime.getStartingDate().isAfter(now()))
                .map(this::convertToShowtimeResponse)
                .collect(Collectors.toList());
    }

    public List<ShowtimeResponse> allShowtime(){
        return showtimeRepository.findAll().stream().map(this::convertToShowtimeResponse).collect(Collectors.toList());
    }

    public ShowtimeResponse convertToShowtimeResponse(Showtime showtime){
        ShowtimeResponse showtimeResponse = modelMapper.map(showtime, ShowtimeResponse.class);
        showtimeResponse.setMovieName(showtime.getMovie().getTitle());
        showtimeResponse.setTheaterName(showtime.getTheater().getName());
        showtimeResponse.setShowtimeId(showtimeResponse.getShowtimeId());
        showtimeResponse.setMovieId(Math.toIntExact(showtime.getMovie().getMovieId()));
        showtimeResponse.setPricePerSeat(showtime.getTheater().getPricePerSeat());
        showtimeResponse.setScreenType(showtime.getTheater().getScreenType());
        return showtimeResponse;
    }

}
