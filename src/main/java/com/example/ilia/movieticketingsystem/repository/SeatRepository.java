package com.example.ilia.movieticketingsystem.repository;

import com.example.ilia.movieticketingsystem.model.Seat;
import com.example.ilia.movieticketingsystem.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {

    List<Seat> findByTheater(Theater theater);

    Seat findBySeatId(Long seatId);

    @Query(value = "SELECT seat_id FROM reservation_seat WHERE reservation_id = :reservationId", nativeQuery = true)
    List<Integer> findAllSeatIdsByReservationId(@Param("reservationId") int reservationId);


}
