package com.example.ilia.movieticketingsystem.service;


import com.example.ilia.movieticketingsystem.DTO.NewEmailDTO;
import com.example.ilia.movieticketingsystem.DTO.UserOrderResponse;
import com.example.ilia.movieticketingsystem.model.Reservation;
import com.example.ilia.movieticketingsystem.model.Seat;
import com.example.ilia.movieticketingsystem.model.User;
import com.example.ilia.movieticketingsystem.repository.ReservationRepository;
import com.example.ilia.movieticketingsystem.repository.SeatRepository;
import com.example.ilia.movieticketingsystem.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;


    public User getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (User) authentication.getPrincipal();
            return userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        } catch (RuntimeException e) {
            throw new ClassCastException("Not user found!");
        }
    }

    public List<UserOrderResponse> getUsersOrders() {
        User user = getCurrentUser();
        List<UserOrderResponse> userOrderResponseList = new ArrayList<>();

        List<Reservation> allReservationsByUser = reservationRepository.findAllByBuyerEmail(user.getEmail());

        for (Reservation reservation : allReservationsByUser) {
            UserOrderResponse userOrderResponse = new UserOrderResponse();
            userOrderResponse.setShowtimeId(Math.toIntExact(reservation.getShowtime().getShowtimeId()));
            userOrderResponse.setBuyerEmail(reservation.getBuyerEmail());
            userOrderResponse.setListOfSeatsId(getSeatsByReservationId(reservation.getReservationId()));
            userOrderResponse.setFinalCost(getTotalAmount(getSeatsByReservationId(reservation.getReservationId())));

            userOrderResponseList.add(userOrderResponse);
        }

        return userOrderResponseList;

    }

    private List<Integer> getSeatsByReservationId(Long reservationId) {
        return seatRepository.findAllSeatIdsByReservationId(Math.toIntExact(reservationId));
    }

    public String getTotalAmount(List<Integer> list){
        Seat firstSeat = seatRepository.findBySeatId(Long.valueOf(list.get(0)));
        long amount = firstSeat.getTheater().getPricePerSeat()* (long) list.size();
        return String.valueOf((amount + amount * 0.08));
    }

    public void changeEmail(NewEmailDTO newEmailDTO) {
        User user = getCurrentUser();
        reservationRepository.updateEmail(user.getEmail(), newEmailDTO.getNewEmail());
        user.setEmail(newEmailDTO.getNewEmail());
        userRepository.save(user);


        //TODO replace all old emails to new emails with ReservationRepository
    }
}
