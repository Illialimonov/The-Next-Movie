package com.example.ilia.movieticketingsystem.repository;

import com.example.ilia.movieticketingsystem.model.Showtime;
import com.example.ilia.movieticketingsystem.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Integer> {
}
