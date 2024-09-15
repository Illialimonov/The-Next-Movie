package com.example.ilia.movieticketingsystem.service;

import com.example.ilia.movieticketingsystem.model.Seat;
import com.example.ilia.movieticketingsystem.model.Showtime;
import com.example.ilia.movieticketingsystem.repository.ReservationRepository;
import com.example.ilia.movieticketingsystem.repository.SeatRepository;
import com.example.ilia.movieticketingsystem.repository.ShowtimeRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
@AllArgsConstructor
public class SendEmailService {
    private JavaMailSender mailSender;
    private ShowtimeRepository showtimeRepository;
    private SeatRepository seatRepository;
    private ReservationRepository reservationRepository;


    public void sendEmail(String recipient, String resetCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ilialimits222@gmail.com");
        message.setTo(recipient);
        message.setText("Your code to reset the password is: " + resetCode);
        message.setSubject("Code for resetting your password");

        mailSender.send(message);

        System.out.println("Sent successfully");

    }

//    public void contactSeller(String messageContent, String recipient, String listingName) {
//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//            helper.setFrom("ilialimits222@gmail.com");
//            helper.setTo(recipient);
//            helper.setSubject("Regarding: " + listingName);
//
//            // Customize your HTML message with a specific font
//            String htmlContent = "<html>" +
//                    "<body style='font-family: Arial, sans-serif;'>" +
//                    "<h2>Message from: " + userService.getCurrentUser().getEmail() + "</h2>" +  // Include sender's name
//                    "<h3>Regarding your listing: " + listingName + "</h3>" +
//                    "<p>" + messageContent + "</p>" +
//                    "</body>" +
//                    "</html>";
//
//            helper.setText(htmlContent, true); // true indicates it's an HTML message
//
//            mailSender.send(message);
//            System.out.println("Sent successfully");
//        } catch (Exception e) {
//            System.out.println("Error sending email: " + e.getMessage());
//        }
//    }

    public void sendConfirmation(String recipient, int showtimeId, List<Integer> seatIds, String totalAmount) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("next.movie.helper@gmail.com");
            helper.setTo(recipient);
            helper.setSubject("Confirmation from Next Movie!");

            // Customize your HTML message with a specific font
            String htmlContent = "<html>\n" +
                    "  <head>\n" +
                    "    <meta http-equiv=\"Content-Type\" content=\"text/html charset=UTF-8\" />\n" +
                    "  </head>\n" +
                    "  <body style=\"font-family: Arial, sans-serif\">\n" +
                    "    <table align=\"center\" width=\"600\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"color: #ffffff; text-align: center\" bgcolor=\"#120c0d\">\n" +
                    "      <tr class=\"text-left align-middle\">\n" +
                    "        <td style=\"height: 100px; text-align: center; vertical-align: middle\">\n" +
                    "          <span style=\"vertical-align: middle\">\n" +
                    "            <svg viewbox=\"0 0 24 24\" width=\"32\" height=\"32\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
                    "              <path\n" +
                    "                d=\"M22 10.75c.41 0 .75-.34.75-.75V9c0-4.41-1.34-5.75-5.75-5.75h-6.25V5.5c0 .41-.34.75-.75.75s-.75-.34-.75-.75V3.25H7C2.59 3.25 1.25 4.59 1.25 9v.5c0 .41.34.75.75.75.96 0 1.75.79 1.75 1.75S2.96 13.75 2 13.75c-.41 0-.75.34-.75.75v.5c0 4.41 1.34 5.75 5.75 5.75h2.25V18.5c0-.41.34-.75.75-.75s.75.34.75.75v2.25H17c4.41 0 5.75-1.34 5.75-5.75 0-.41-.34-.75-.75-.75-.96 0-1.75-.79-1.75-1.75s.79-1.75 1.75-1.75Zm-11.25 3.42c0 .41-.34.75-.75.75s-.75-.34-.75-.75V9.83c0-.41.34-.75.75-.75s.75.34.75.75v4.34Z\"\n" +
                    "                fill=\"#3caf2c\"\n" +
                    "              />\n" +
                    "            </svg>\n" +
                    "          </span>\n" +
                    "          <span style=\"vertical-align: middle; font-size: 28px; margin-left: 10px\">\n" +
                    "            <b>Next Movie</b>\n" +
                    "          </span>\n" +
                    "        </td>\n" +
                    "      </tr>\n" +
                    "      <tr>\n" +
                    "        <td>\n" +
                    "          <!-- CUSTOMER INFO -->\n" +
                    "\n" +
                    "          <b style=\"margin-bottom: 50px; font-size: 20px\">Thank you for your purchase, Dear Customer!</b>\n" +
                    "          <br />\n" +
                    "          <p style=\"color: #757575\">We're excited to confirm your ticket purchase for:</p>\n" +
                    "          <br />\n" +
                    "          <b>" + getMovieName(showtimeId) + "</b>\n" +
                    "          <br /><br /><br />\n" +
                    "\n" +
                    "          <!-- POSTER -->\n" +
                    "\n" +
                    "          <img src=\"" + getPosterURL(showtimeId) + "\" alt=\"poster\" width=\"250\" height=\"250\" />\n" +
                    "\n" +
                    "          <!-- DATE & TIME -->\n" +
                    "\n" +
                    "          <p style=\"margin-top: 50px\">Date & Time: " + getEventAndDateData(showtimeId) + "</p>\n" +
                    "\n" +
                    "          <!-- THEATER -->\n" +
                    "\n" +
                    "          <p>Theater: " + getTheaterName(showtimeId) + "</p>\n" +
                    "          <br /><br /><br /><br />\n" +
                    "          <b style=\"font-size: 24px\">Ticket Details:</b>\n" +
                    "\n" +
                    "          <!-- ROWS AND SEATS -->\n" +
                    "\n" +
                    "          <p>Reserved Seats: <i style=\"color: #757575\">" + getSeatData(seatIds) + "</i></p>\n" +
                    "\n" +
                    "          <!-- NUMBER OF TICKETS -->\n" +
                    "\n" +
                    "          <p>Quantity: <i style=\"color: #757575\">" + getTicketQuantity(seatIds) + "</i></p>\n" +
                    "\n" +
                    "          <!-- TICKET ID -->\n" +
                    "\n" +
                    "          <p>Ticket ID: <i style=\"color: #757575\">" + getReservationId() + "</i></p>\n" +
                    "          <br />\n" +
                    "          <h4 style=\"font-size: 24px\">Order Summary:</h4>\n" +
                    "\n" +
                    "          <!-- TOTAL PAID -->\n" +
                    "\n" +
                    "          <p>Total Paid: <i style=\"color: #757575\">" + totalAmount + "</i></p>\n" +
                    "\n" +
                    "          <!-- PAYMENT METHOD -->\n" +
                    "\n" +
                    "          <p>Payment Method: <i style=\"color: #757575\">Stripe</i></p>\n" +
                    "          <br />\n" +
                    "          <h4 style=\"font-size: 24px\">Event Information:</h4>\n" +
                    "          <p style=\"padding: 0 100px\"><i style=\"color: #757575\">Please arrive 15 minutes prior the beginning. Bring a valid ID and a copy of your ticket (either printed or on your phone).</i></p>\n" +
                    "          <br />\n" +
                    "          <h4 style=\"font-size: 24px\">Contact Us:</h4>\n" +
                    "\n" +
                    "          <!-- SUPPORT INFO. NOTE: DYNAMIC TEXT AND HREF VALUES IN <a></a> TAGS -->\n" +
                    "\n" +
                    "          <p style=\"padding: 0 100px\">\n" +
                    "            <i style=\"color: #757575\"\n" +
                    "              >If you have any questions, feel free to reach out to us at <a href=\"mailto:next.movie.helper@gmail.com\" style=\"color: #4160b1\">next.movie.helper@gmail.com</a> or call us at\n" +
                    "              <a href=\"tel:+17273583406\" style=\"color: #4160b1\">+17273583406</a>.</i\n" +
                    "            >\n" +
                    "          </p>\n" +
                    "\n" +
                    "          <p style=\"margin-top: 50px; color: #757575\">We look forward to seeing you at the event!</p>\n" +
                    "          <p>\n" +
                    "            Sincerely, <br/>\n" +
                    "            Next Movie\n" +
                    "          </p>\n" +
                    "        </td>\n" +
                    "      </tr>\n" +
                    "    </table>\n" +
                    "  </body>\n" +
                    "</html>";

            helper.setText(htmlContent, true); // true indicates it's an HTML message

            mailSender.send(message);
            System.out.println("Sent successfully");
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
        }

    }

    // get all the necessary info for the info

    public String getMovieName(int showtimeId) {
        return showtimeRepository.findById(showtimeId).orElseThrow().getMovie().getTitle();
    }

    private String getPosterURL(int showtimeId) {
        return showtimeRepository.findById(showtimeId).orElseThrow().getMovie().getPosterURL();
    }

    private String getSeatData(List<Integer> seatsIds) {
        List<Seat> seats = seatsIds.stream().map(seatId -> seatRepository.findBySeatId((long) seatId)).toList();
        StringBuilder sb = new StringBuilder();
        for (Seat seat : seats) {
            sb.append("Row ").append(seat.getRow_num()).append(", Seat ").append(seat.getSeat_num()).append("<br/>");
        }

        return sb.toString();
    }

    private String getTheaterName(int showtimeId) {
        return showtimeRepository.findById(showtimeId).orElseThrow().getTheater().getName();
    }

    private String getEventAndDateData(int showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId).orElseThrow();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return (showtime.getStartingDate().format(formatter));
    }


    private int getTicketQuantity(List<Integer> list) {
        return Math.toIntExact(list.size());
    }

    private String getReservationId() {
        return String.valueOf(System.currentTimeMillis());
    }


}
