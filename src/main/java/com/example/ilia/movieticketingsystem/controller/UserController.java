package com.example.ilia.movieticketingsystem.controller;

import com.example.ilia.movieticketingsystem.DTO.NewEmailDTO;
import com.example.ilia.movieticketingsystem.DTO.UserOrderResponse;
import com.example.ilia.movieticketingsystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/orders/")
    public List<UserOrderResponse> getAllUserReservation(){
        return userService.getUsersOrders();
    }

    @PostMapping("/change-email")
    public ResponseEntity<?> changeEmail(@RequestBody NewEmailDTO newEmailDTO){
        userService.changeEmail(newEmailDTO);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
