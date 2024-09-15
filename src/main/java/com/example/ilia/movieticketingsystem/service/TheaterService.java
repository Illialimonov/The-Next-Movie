package com.example.ilia.movieticketingsystem.service;

import com.example.ilia.movieticketingsystem.DTO.TheaterDTO;
import com.example.ilia.movieticketingsystem.model.Theater;
import com.example.ilia.movieticketingsystem.repository.TheaterRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TheaterService {
    private final TheaterRepository theaterRepository;
    private final ModelMapper modelMapper;

    public List<TheaterDTO> getAllTheaters() {
        return theaterRepository.findAll().stream().map(this::convertToTheaterDTO)
                .collect(Collectors.toList());
    }

    public TheaterDTO convertToTheaterDTO(Theater theater){
        return modelMapper.map(theater, TheaterDTO.class);
    }
}
