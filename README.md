# The Next Movie
<img width="55" alt="Screenshot 2024-11-10 at 11 06 56 AM" src="https://github.com/user-attachments/assets/9ab0a473-96af-4556-be42-ba35281b2b4d">

## Project Overview
**The Next Movie** is a movie booking system, developed as a RESTful API with Spring Boot, aimed at simulating an online platform where customers can book movie tickets, browse through categories and check their transaction history. Theater owners can manage movies and showtimes through a well-designed admin panel. 

## Features

### User Features
- **Browse Available Movies**: Users can view a list of movies available for booking.
- **Book Tickets**: Users can select a movie and book tickets for available showtimes while choosing seats they like.
- **Email Confirmations**: After successfull payments users will recieve an email confirmation with all the details included.

### Admin Features
- **Add New Movies**: Admins can add new movies to the system.
- **Add Showtimes**: Admins can add showtimes for movies.
- **Manage Data**: Admins can manage movie and booking data through a secured endpoint.

## Database Management with PostgreSQL
The system uses **PostgreSQL** for secure data storage.

The database schema includes the following tables:
- **Users**: Stores information about registered users and their details.
- **Movies**: Stores details about movies such as title, description, and genre.
- **Showtimes**: Stores showtimes for when movies are being shown.
- **Reservations**: Stores user bookings for specific showtimes.
- **Seats**: Stores seats' information for each theater.


## Payment Processing with Stripe
- **Stripe Webhooks** are used to handle secure payment processing.
- After a successful booking, users receive a **confirmation email** with booking details.
- Payment processing is secure, and email notifications are sent promptly after successful transactions.


## My Contribution to The Next Movie

In **The Next Movie** project, I was responsible for building the backend functionality to create a complete movie booking experience. Using Spring Boot, I developed a REST API that enabled users to browse movies and reserve tickets, while theater owners could add and manage movies and showtimes via an admin-only endpoint. I set up complex relationships between entities and managed efficient data retrieval using Hibernate ORM. Handling payments was a key part of the project, so I integrated Stripe to securely process transactions, using webhooks to automatically send email confirmations for successful reservations. To maintain high reliability, I wrote extensive unit and integration tests with JUnit and Mockito, helping to catch and resolve potential issues early. I also used Amazon S3 buckets to store photos of newly added movies

## Application Interface

Landing page:
<img width="1427" alt="Screenshot 2024-11-10 at 1 37 35 PM" src="https://github.com/user-attachments/assets/9778bf8d-b29b-49f4-9b01-87f1498ff1e5">

Book a ticket:
<img width="1424" alt="Screenshot 2024-11-10 at 1 38 19 PM" src="https://github.com/user-attachments/assets/29ae8e8c-b063-4549-818f-1171cf61719e">

Finilize payments:
<img width="1440" alt="Screenshot 2024-11-10 at 1 38 52 PM" src="https://github.com/user-attachments/assets/590eca67-d62e-4238-a781-4bdcaea8e4e8">

Personal Stripe Checkout:
<img width="1428" alt="Screenshot 2024-11-10 at 1 39 03 PM" src="https://github.com/user-attachments/assets/627e7c9b-d0aa-4ce4-b033-841fdda1abd0">

Browse movies:
<img width="1427" alt="Screenshot 2024-11-10 at 1 39 23 PM" src="https://github.com/user-attachments/assets/227a70ec-f22b-4394-886c-aab90427be2e">

Admin panel:
<img width="1429" alt="Screenshot 2024-11-10 at 1 43 13 PM" src="https://github.com/user-attachments/assets/e47ff2ca-6af3-449b-9757-10b9ac85af67">











The website can be found here: [The Next Movie](https://thenextmovie.com)



