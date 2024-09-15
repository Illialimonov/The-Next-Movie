package com.example.ilia.movieticketingsystem.repository;

import com.example.ilia.movieticketingsystem.model.Reservation;
import com.example.ilia.movieticketingsystem.model.Seat;
import com.example.ilia.movieticketingsystem.model.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByShowtime(Showtime showtime);

    boolean existsByShowtimeAndSeatListIn(Showtime showtime, List<Integer> seatIds);

    @Query("SELECT r.seatList FROM Reservation r WHERE r.showtime.showtimeId = :showtimeId")
    List<Seat> findAllReservedSeatsByShowtimeId(@Param("showtimeId") int showtimeId);

    List<Reservation> findAllByBuyerEmail(String buyerEmail);

    @Transactional
    @Modifying
    @Query("UPDATE Reservation o SET o.buyerEmail = :newEmail WHERE o.buyerEmail = :oldEmail")
    void updateEmail(@Param("oldEmail") String oldEmail, @Param("newEmail") String newEmail);

}
