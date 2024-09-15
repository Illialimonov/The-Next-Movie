package com.example.ilia.movieticketingsystem.service;

import com.example.ilia.movieticketingsystem.DTO.ReservationDTO;
import com.example.ilia.movieticketingsystem.model.Reservation;
import com.example.ilia.movieticketingsystem.model.Showtime;
import com.example.ilia.movieticketingsystem.model.Theater;
import com.example.ilia.movieticketingsystem.repository.ReservationRepository;
import com.example.ilia.movieticketingsystem.repository.SeatRepository;
import com.example.ilia.movieticketingsystem.repository.ShowtimeRepository;
import com.example.ilia.movieticketingsystem.util.ShowtimeOperationException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;

    public void makeReservation(ReservationDTO reservationDTO) {
        List<Integer> seatIds = reservationDTO.getListOfSeatsId();

//        if (authentication != null && authentication.isAuthenticated()) {
//            // Call method for authenticated users
//
//        } else {
//            // Call method for unauthenticated users
//        }

        // Determine if the seat IDs are valid
        if (areSeatsValid(reservationDTO)) {

            Reservation reservation = new Reservation();
            reservation.setSeatList(seatRepository.findAllById(seatIds));
            reservation.setShowtime(showtimeRepository.findById(reservationDTO.getShowtimeId()).orElseThrow());
            reservation.setBuyerEmail(reservationDTO.getBuyerEmail());

            // Save the reservation
            reservationRepository.save(reservation);
        }
    }

    public boolean areSeatsValid(ReservationDTO reservationDTO) {
        List<Integer> seatIds = reservationDTO.getListOfSeatsId();

        // Fetch the showtime and theater
        Showtime showtime = showtimeRepository.findById(reservationDTO.getShowtimeId()).orElseThrow();
        Theater theater = showtime.getTheater();

        int lowerBoundary = theater.getLowerBoundarySeatId();
        int upperBoundary = theater.getUpperBoundarySeatId();

        // Check if all seat IDs are within the valid range
        boolean areAllSeatsWithinBoundary = seatIds.stream().allMatch(seatId -> seatId >= lowerBoundary && seatId <= upperBoundary);

        if (!areAllSeatsWithinBoundary) {
            throw new ShowtimeOperationException("One or more seats are not within the valid range for this showtime.");
        }

        boolean isAnySeatAlreadyReserved = reservationRepository.existsByShowtimeAndSeatListIn(
                showtime, seatIds
        );

        if (isAnySeatAlreadyReserved) {
            // Handle the situation (e.g., throw an exception)
            throw new ShowtimeOperationException("One or more seats are already reserved for this showtime.");
        }

        return true;
    }



}
