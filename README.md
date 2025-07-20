# ParkingSystemBackend

A Spring Boot backend for a smart parking management system, supporting user authentication, parking spot management, booking, payments (Stripe), and vehicle log tracking.

## Features
- **User Authentication**: JWT-based signup, login, and password change for buyers and sellers.
- **Parking Management**: Owners can create, update, and delete parking spots with details like location, slots, and amenities.
- **Booking System**: Renters can book available parking slots, view/cancel bookings, and check availability.
- **Payment Integration**: Stripe-based payment processing for bookings, with support for card management and transaction logs.
- **Vehicle Logs**: Track vehicle entries/exits and match with bookings for security and analytics.
- **Admin/User Dashboards**: Summaries for owners (total parkings, slots, bookings, earnings, etc.).

## Tech Stack
- Java 17
- Spring Boot 3.4.2
- Spring Data JPA (MySQL)
- Spring Security (JWT)
- Stripe Java SDK
- Lombok
- Maven

## Getting Started

### Prerequisites
- Java 17+
- Maven
- MySQL

### Setup
1. **Clone the repository**
   ```bash
   git clone <repo-url>
   cd ParkingSystemBackend
   ```
2. **Configure Database**
   - Create a MySQL database named `parkpro_db` (or update `src/main/resources/application.properties`).
   - Default credentials in `application.properties`:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/parkpro_db
     spring.datasource.username=root
     spring.datasource.password=root
     ```
3. **Stripe Configuration**
   - Set your Stripe secret key in `application.properties`:
     ```properties
     STRIPE_PUBLIC_KEY=sk_test_...  # Replace with your key
     ```
4. **Build and Run**
   ```bash
   ./mvnw spring-boot:run
   ```
   The server runs on `http://localhost:8080` by default.

## API Overview

### Authentication
- `POST /auth/signup` — Register a new user
- `POST /auth/login` — Login and receive JWT
- `PUT /auth/change-password` — Change password

### Parking
- `POST /api/parking/createParking` — Create parking spot (owner)
- `PUT /api/parking/updateParking/{id}` — Update parking
- `GET /api/parking/getUserParkings?userId=...` — Get parkings by owner
- `GET /api/parking/getAllParking` — List all parkings
- `GET /api/parking/check/availability` — Check slot availability
- `GET /api/parking/{id}` — Get parking by ID
- `DELETE /api/parking/{id}` — Delete parking
- `GET /api/parking/dashboard/host-summary?ownerId=...` — Owner dashboard summary

### Booking
- `POST /api/booking/createBooking` — Create booking
- `GET /api/booking/getAllBookings` — List all bookings
- `GET /api/booking/user/{userId}` — Get bookings by user
- `GET /api/booking/owner/{ownerId}` — Get bookings by owner
- `GET /api/booking/byParking/{parkingId}` — Get bookings for a parking
- `GET /api/booking/{id}` — Get booking by ID
- `PUT /api/booking/cancel/{id}` — Cancel booking
- `PUT /api/booking/confirm/{id}/{VehicleDetected}` — Confirm booking (vehicle detected)
- `PUT /api/booking/unmatched-checkin/{id}/{VehicleDetected}` — Unmatched check-in
- `DELETE /api/booking/delete/{id}` — Delete booking
- `GET /api/booking/unmatched-checkins/{ownerId}` — List unmatched check-ins
- `PUT /api/booking/mark-visited` — Mark unmatched check-ins as visited

### Payments
- `GET /api/payments/getAllPayment` — List all payments
- `POST /stripe/payment/add` — Add card to Stripe customer
- `POST /stripe/payment/charge` — Charge a card
- `POST /stripe/payment/make-payment` — Make a payment

### Users
- `GET /api/user/getAllUsers` — List all users
- `POST /api/user/createUser` — Create user
- `GET /api/user/{id}` — Get user by ID

### Vehicles
- `GET /api/vehicle/logs/{bookingId}` — Get vehicle logs for a booking

## Data Model (Entities)
- **User**: id, name, email, password, phoneNumber, role (BUYER/SELLER)
- **Parking**: id, title, address, description, location, charges, slots, amenities, owner
- **ParkingSlot**: id, slotNumber, isActive, parking
- **Booking**: id, renter, parkingSlot, parking, vehicleNumber, start/end time, totalPrice, status
- **Payment**: id, booking, user, amount, status, method, transactionId, paidAt
- **VehicleLog**: id, plateNumber, detectedAt, direction, status, parking, matchedBooking

## Configuration
See `src/main/resources/application.properties` for all config options:
- Database connection
- JWT secret and expiration
- Stripe public key

## Testing
- Unit tests: `src/test/java/com/example/parkingsystembackend/service/`
- Run all tests:
  ```bash
  ./mvnw test
  ```

## License
This project is for educational/demo purposes. Add your license as needed. 