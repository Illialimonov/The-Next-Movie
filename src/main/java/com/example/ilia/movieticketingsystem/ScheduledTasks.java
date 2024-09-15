package com.example.ilia.movieticketingsystem;

import com.example.ilia.movieticketingsystem.repository.ShowtimeRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class ScheduledTasks {
    private final ShowtimeRepository showtimeRepository;



}
