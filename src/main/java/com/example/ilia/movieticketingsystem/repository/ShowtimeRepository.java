package com.example.ilia.movieticketingsystem.repository;

import com.example.ilia.movieticketingsystem.model.Showtime;
import com.example.ilia.movieticketingsystem.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Integer> {

    @Query("SELECT t FROM Theater t JOIN Showtime s ON s.theater.theaterId = t.theaterId WHERE s.showtimeId = :showtimeId")
    Theater findTheaterByShowtimeId(@Param("showtimeId") int showtimeId);

    @Query(value = "SELECT FROM showtime WHERE starting_date < :currentDateTime", nativeQuery = true)
    void selectShowtimeByEndingDatePastNow(@Param("currentDateTime") LocalDateTime currentDateTime);

}