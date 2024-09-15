package com.example.ilia.movieticketingsystem.service;

import com.example.ilia.movieticketingsystem.DTO.ReservationDTO;
import com.example.ilia.movieticketingsystem.DTO.Response.CheckoutSessionResponse;
import com.example.ilia.movieticketingsystem.DTO.Response.checkoutComponents.MovieComponentResponse;
import com.example.ilia.movieticketingsystem.DTO.Response.checkoutComponents.PricingComponentResponse;
import com.example.ilia.movieticketingsystem.DTO.Response.checkoutComponents.SeatComponentResponse;
import com.example.ilia.movieticketingsystem.DTO.Response.checkoutComponents.ShowtimeComponentResponse;
import com.example.ilia.movieticketingsystem.model.Movie;
import com.example.ilia.movieticketingsystem.model.Seat;
import com.example.ilia.movieticketingsystem.model.Showtime;
import com.example.ilia.movieticketingsystem.repository.SeatRepository;
import com.example.ilia.movieticketingsystem.repository.ShowtimeRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@AllArgsConstructor
public class PaymentService {
    private final SeatRepository seatRepository;
    private final ShowtimeRepository showtimeRepository;
    private final ModelMapper modelMapper;
    private final ReservationService reservationService;
    private final SendEmailService sendEmailService;
    private final UserService userService;

    public String generateStripeLink(@RequestBody ReservationDTO reservationDTO, Authentication authentication){
        try {
            // Create a new Checkout Session
            SessionCreateParams params =
                    SessionCreateParams.builder()
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setSuccessUrl("https://next-movie-booking.vercel.app/success")
                            .setCancelUrl("https://next-movie-booking.vercel.app/")
                            // put all metadata for the webhook
                            .putAllMetadata(Map.of(
                                    "seat_ids", String.join(",", reservationDTO.getListOfSeatsId().stream().map(String::valueOf).toArray(String[]::new)),
                                    "showtime_id", String.valueOf(reservationDTO.getShowtimeId()),
                                    "buyer_email", getBuyerEmail(reservationDTO, authentication),
                                    "total_amount", String.valueOf((getTotalAmount(reservationDTO) + getTotalAmount(reservationDTO) * 0.08))

                            ))
                            .addLineItem(
                                    SessionCreateParams.LineItem.builder()
                                            .setQuantity(1L)
                                            .setPriceData(
                                                    SessionCreateParams.LineItem.PriceData.builder()
                                                            .setCurrency("usd")
                                                            .setUnitAmount((long) ((getTotalAmount(reservationDTO) + getTotalAmount(reservationDTO) * 0.08) * 100))
                                                            .setProductData(
                                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                            .setName("Movie Tickets")
                                                                            .setDescription(getSeatsAmount(reservationDTO) + " tickets for: " + getMovieName(reservationDTO) + " at " + convertToHumanReadableString(getShowtimeTime(reservationDTO)))
                                                                            .build()
                                                            )
                                                            .build()

                                            )
                                            .build()

                            )
                            .build();

            // Create the session
            Session session = Session.create(params);

            // Return the URL of the session
            return session.getUrl();

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String getBuyerEmail(ReservationDTO reservationDTO, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return userService.getCurrentUser().getEmail();
        } else {
            return reservationDTO.getBuyerEmail();

        }
    }

    public CheckoutSessionResponse composeCheckoutResponse(ReservationDTO reservationDTO){
        if (reservationService.areSeatsValid(reservationDTO)) {
            //Whole response created

            CheckoutSessionResponse checkoutSessionResponse = new CheckoutSessionResponse();

            //Movie Component below
            Movie movie = showtimeRepository.findById(reservationDTO.getShowtimeId()).orElseThrow().getMovie();
            MovieComponentResponse movieComponentResponse = modelMapper.map(movie, MovieComponentResponse.class);

            //Pricing Component below
            PricingComponentResponse pricingComponentResponse = new PricingComponentResponse();

            pricingComponentResponse.setPricePerTicket(getPricePerTicket(reservationDTO));
            pricingComponentResponse.setSubtotal(getTotalAmount(reservationDTO));
            pricingComponentResponse.setDiscounts(0);
            pricingComponentResponse.setTaxes(getTotalAmount(reservationDTO) * 0.08);
            pricingComponentResponse.setTotalAmount(getTotalAmount(reservationDTO) + getTotalAmount(reservationDTO) * 0.08);


            //Seat Component below
            SeatComponentResponse seatComponentResponse = new SeatComponentResponse();

            seatComponentResponse.setSeats(composeMapOfSeats(reservationDTO));

            //Showtime Component below
            Showtime showtime = showtimeRepository.findById(reservationDTO.getShowtimeId()).orElseThrow();
            ShowtimeComponentResponse showtimeComponentResponse = modelMapper.map(showtime, ShowtimeComponentResponse.class);
            showtimeComponentResponse.setTheater(showtime.getTheater().getName());
            showtimeComponentResponse.setShowtimeId(reservationDTO.getShowtimeId());


            //Final Step
            checkoutSessionResponse.setMovieComponentResponse(movieComponentResponse);
            checkoutSessionResponse.setPricingComponentResponse(pricingComponentResponse);
            checkoutSessionResponse.setSeatComponentResponse(seatComponentResponse);
            checkoutSessionResponse.setShowtimeComponentResponse(showtimeComponentResponse);

            return checkoutSessionResponse;
        }

        return null;

    }

    public ResponseEntity<String> handleStripePayment(String payload, String sigHeader){
        Event event;

        System.out.println("are you here?");

        try {
            event = Webhook.constructEvent(payload, sigHeader, "whsec_P7mfKO8qNA3NVW0iIg0LulFa3SYbxNB2");
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing event");
        }
        Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);

        // Handle unknown event types
        if (event.getType().equals("checkout.session.completed")) {// Retrieve session details and update reservation
            System.out.println("you here?");
            if (session != null) {
                System.out.println("transaction completed");
                //Map metadata from the checkout session to the ReservationDTO
                ReservationDTO reservationDTO = new ReservationDTO();
                reservationDTO.setBuyerEmail(extractBuyerEmailFromSession(session));
                reservationDTO.setShowtimeId(extractShowtimeIdFromSession(session));
                reservationDTO.setListOfSeatsId(extractListOfSeatsFromSession(session));

                reservationService.makeReservation(reservationDTO);

                // Send the check online via email
                sendEmailService.sendConfirmation(extractBuyerEmailFromSession(session), extractShowtimeIdFromSession(session), extractListOfSeatsFromSession(session), extractTotalAmountFromSession(session));


            }
        } else {
            throw new RuntimeException("Error whith Stripe");
        }

        return ResponseEntity.ok("Received");

    }

    public String extractBuyerEmailFromSession(Session session){
        Map<String, String> metadata = session.getMetadata();
        return metadata.get("buyer_email");
    }

    public int extractShowtimeIdFromSession(Session session){
        Map<String, String> metadata = session.getMetadata();
        return Integer.parseInt(metadata.get("showtime_id"));
    }

    public String extractTotalAmountFromSession(Session session){
        Map<String, String> metadata = session.getMetadata();
        return metadata.get("total_amount");
    }



    public List<Integer> extractListOfSeatsFromSession(Session session){
        Map<String, String> metadata = session.getMetadata();
        String unparsedSeatIds = metadata.get("seat_ids");
        return Arrays.stream(unparsedSeatIds.split(","))
                .map(Integer::valueOf)  // Convert each String to Integer
                .toList();  // Collect the results into a List
    }

    public Long getTotalAmount(ReservationDTO reservationDTO){
        Seat firstSeat = seatRepository.findBySeatId(Long.valueOf(reservationDTO.getListOfSeatsId().get(0)));
        return firstSeat.getTheater().getPricePerSeat()* (long) reservationDTO.getListOfSeatsId().size();
    }


    public Long getSeatsAmount(ReservationDTO reservationDTO){
        return (long) reservationDTO.getListOfSeatsId().size();
    }

    public int getPricePerTicket(ReservationDTO reservationDTO){
        Seat firstSeat = seatRepository.findBySeatId(Long.valueOf(reservationDTO.getListOfSeatsId().get(0)));
        return firstSeat.getTheater().getPricePerSeat();
    }

    public String getMovieName(ReservationDTO reservationDTO){
        return showtimeRepository.findById(reservationDTO.getShowtimeId()).orElseThrow().getMovie().getTitle();
    }

    public LocalDateTime getShowtimeTime(ReservationDTO reservationDTO){
        return showtimeRepository.findById(reservationDTO.getShowtimeId()).orElseThrow().getStartingDate();
    }

    public static String convertToHumanReadableString(LocalDateTime dateTime) {
        // Define the desired format. Adjust as needed.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the LocalDateTime using the formatter
        return dateTime.format(formatter);
    }

    public List<HashMap<String, Integer>> composeMapOfSeats(ReservationDTO reservationDTO){
        List<HashMap<String, Integer>> toReturn = new ArrayList<>();

        for (Integer seatId : reservationDTO.getListOfSeatsId()){
            HashMap<String, Integer> oneSeatNaming = new HashMap<>();
            Seat seat = seatRepository.findById(seatId).orElseThrow();
            oneSeatNaming.put("Row#: ", seat.getRow_num());
            oneSeatNaming.put("Seat#: ", seat.getSeat_num());
            toReturn.add(oneSeatNaming);
        }

        return toReturn;

    }
}
