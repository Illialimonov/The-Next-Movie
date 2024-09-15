package com.example.ilia.movieticketingsystem.repository;

import com.example.ilia.movieticketingsystem.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
}
