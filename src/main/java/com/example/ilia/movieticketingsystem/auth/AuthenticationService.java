package com.example.ilia.movieticketingsystem.auth;

import com.example.ilia.movieticketingsystem.DTO.GoogleLoginRequest;
import com.example.ilia.movieticketingsystem.config.JwtService;
import com.example.ilia.movieticketingsystem.model.RefreshToken;
import com.example.ilia.movieticketingsystem.model.User;
import com.example.ilia.movieticketingsystem.repository.RefreshTokenRepository;
import com.example.ilia.movieticketingsystem.repository.UserRepository;
import com.example.ilia.movieticketingsystem.service.RefreshTokenService;
import com.example.ilia.movieticketingsystem.service.SendEmailService;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 6;
    private static final Random RANDOM = new SecureRandom();
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;



    public void register(RegisterRequest input) {


        User user = new User();
        user.setFirstName(input.getFirstName());
        user.setLastName(input.getLastName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setRole("ROLE_USER");


        userRepository.save(user);
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        if(authentication.isAuthenticated()) {
            var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

            var jwtToken = jwtService.generateToken(user);

            refreshTokenRepository.deleteByUser_id(user.getId());

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());


            HashMap<String,String> userDetails = new HashMap<>();
            userDetails.put("id", user.getId().toString());
            userDetails.put("email", user.getEmail());
            userDetails.put("firstName",user.getFirstName());
            userDetails.put("lastName",user.getLastName());
            userDetails.put("role",user.getRole());

            return AuthenticationResponse.builder()
                    .access_token(jwtToken)
                    .refresh_token(refreshToken.getToken())
                    .userDetails(userDetails)
                    .build();

        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }

    }


    public static String generateCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }

    public boolean userIsNotInDB(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }


    public ResponseEntity<?> loginWithGoogle(GoogleLoginRequest loginRequest) throws GeneralSecurityException, IOException {
        // Extract client_id and credentials from the request
        String clientId = loginRequest.getClientId(); // Get client_id from request
        String credentials = loginRequest.getCredentials(); // Token sent from frontend

        // GoogleIdTokenVerifier to verify the Google token
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId)) // Use clientId from request
                .build();

        // Verify the credentials (ID token)
        GoogleIdToken idToken = verifier.verify(credentials);

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            // Get user info from the payload
            String email = payload.getEmail();
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");

            if (userIsNotInDB(email)) {
                register(new RegisterRequest(firstName,lastName,clientId, email));
                return ResponseEntity.ok(authenticate(new AuthenticationRequest(email, clientId)));
            } else {
                return ResponseEntity.ok(authenticate(new AuthenticationRequest(email, clientId)));
            }

        }
        return ResponseEntity.badRequest().body("Invalid ID token.");
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenService.findByToken(refreshTokenRequestDTO.getToken());

        if (optionalRefreshToken.isEmpty()) {
            // If the refresh token is not found, throw an exception
            throw new RuntimeException("Refresh Token is not in DB..!!");
        }

        // Get the refresh token from the Optional
        RefreshToken refreshToken = optionalRefreshToken.get();

        // Verify the expiration of the refresh token
        try {
            refreshToken = refreshTokenService.verifyExpiration(refreshToken);
        } catch (RuntimeException e) {
            // If the refresh token is expired, an exception will be thrown
            throw new RuntimeException(e.getMessage());
        }

        // Get the user information associated with the refresh token
        User user = refreshToken.getOwner();
        // Generate a new access token using the user's username
        String accessToken = jwtService.generateToken(user);


        // Create and return the response containing the new access token and the refresh token
        HashMap<String,String> userDetails = new HashMap<>();
        userDetails.put("id", user.getId().toString());
        userDetails.put("email", user.getEmail());
        userDetails.put("firstName",user.getFirstName());
        userDetails.put("lastName",user.getLastName());
        userDetails.put("role",user.getRole());

        return AuthenticationResponse.builder()
                .access_token(accessToken)
                .refresh_token(refreshToken.getToken())
                .userDetails(userDetails)
                .build();
    }
}
