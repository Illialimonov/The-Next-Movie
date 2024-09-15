package com.example.ilia.movieticketingsystem.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowtimeExceptionResponse{
    private String message;
    private long timestamp;

    public ShowtimeExceptionResponse(String message, long timestamp) {
        this.timestamp = timestamp;
        this.message = message;
    }
}

