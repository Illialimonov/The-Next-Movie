package com.example.ilia.movieticketingsystem.service;

import com.example.ilia.movieticketingsystem.DTO.Response.AllSeatsPerShowtimesResponse;
import com.example.ilia.movieticketingsystem.DTO.Response.ShowtimeResponse;
import com.example.ilia.movieticketingsystem.DTO.Seats.AvailableSeatDTO;
import com.example.ilia.movieticketingsystem.DTO.Seats.SeatDTO;
import com.example.ilia.movieticketingsystem.DTO.Seats.SeatsResponseDTO;
import com.example.ilia.movieticketingsystem.model.Reservation;
import com.example.ilia.movieticketingsystem.model.Seat;
import com.example.ilia.movieticketingsystem.model.Showtime;
import com.example.ilia.movieticketingsystem.repository.ReservationRepository;
import com.example.ilia.movieticketingsystem.repository.SeatRepository;
import com.example.ilia.movieticketingsystem.repository.ShowtimeRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SeatService {
    private final SeatRepository seatRepository;
    private final ShowtimeRepository showtimeRepository;
    private final ReservationRepository reservationRepository;
    private final ModelMapper modelMapper;
    private final ShowtimeService showtimeService;

    public List<SeatDTO> getAllSeats(int showtimeId) {
        return seatRepository.findByTheater(showtimeRepository.findTheaterByShowtimeId(showtimeId)).stream().map(this::convertToSeatDTO).collect(Collectors.toList());
    }

    public List<SeatDTO> getReservedSeats(int showtimeId) {
        List<Seat> seats = reservationRepository.findAllReservedSeatsByShowtimeId(showtimeId);
        return seats.stream().map(this::convertToSeatDTO).collect(Collectors.toList());

    }

    public List<AvailableSeatDTO> getAvailableSeats(int showtimeId) {
        // Retrieve reserved seats for the showtime
        List<SeatDTO> reservedSeats = getReservedSeats(showtimeId);

        // Retrieve all seats for the showtime
        List<SeatDTO> allSeats = getAllSeats(showtimeId);

        // Create a copy of allSeats to remove reserved seats from
        List<SeatDTO> availableSeats = new ArrayList<>(allSeats);

        // Remove reserved seats from the list of all seats to get available seats
        availableSeats.removeAll(reservedSeats);

        // Create SeatsResponseDTO with available and reserved seats
        SeatsResponseDTO responseSeats = new SeatsResponseDTO(availableSeats, reservedSeats,showtimeId);

        // Return the available seats
        return responseSeats.getAllSeats(); // Ensure this method is implemented in SeatsResponseDTO
    }

    public List<AllSeatsPerShowtimesResponse> getFutureShowtimeAvailableSeats(){
        List<AllSeatsPerShowtimesResponse> finalList = new ArrayList<>();

        List<Integer> futureShowtimeId = showtimeService.futureShowtime().stream().map(this::getShowtimeId).toList();
        for(int i : futureShowtimeId){
            AllSeatsPerShowtimesResponse seatsAtShowtime = new AllSeatsPerShowtimesResponse();
            seatsAtShowtime.setShowtimeId(i);
            seatsAtShowtime.setMovieId(Math.toIntExact(showtimeRepository.findById(i).orElseThrow().getMovie().getMovieId()));
            seatsAtShowtime.setTheaterId(showtimeRepository.findTheaterByShowtimeId(i).getTheaterId());
            seatsAtShowtime.setAllSeatsPerShowtime(getAvailableSeats(i));
            finalList.add(seatsAtShowtime);
        }

        return finalList;
    }

    public int getShowtimeId(ShowtimeResponse showtimeResponse){
        return Math.toIntExact(showtimeResponse.getShowtimeId());
    }





    public SeatDTO convertToSeatDTO(Seat seat) {
        return modelMapper.map(seat, SeatDTO.class);
    }

}
