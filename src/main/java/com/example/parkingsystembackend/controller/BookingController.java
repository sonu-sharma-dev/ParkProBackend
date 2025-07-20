package com.example.parkingsystembackend.controller;

import com.example.parkingsystembackend.dto.BookingRequest;
import com.example.parkingsystembackend.dto.BookingResponse;
import com.example.parkingsystembackend.dto.UnmatchedCheckinResponse;
import com.example.parkingsystembackend.entity.Booking;
import com.example.parkingsystembackend.entity.VehicleLog;
import com.example.parkingsystembackend.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booking")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/createBooking")
    public ResponseEntity<?> create(@RequestBody BookingRequest booking) {
        try {
            BookingResponse response = bookingService.createBooking(booking);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getAllBookings")
    public List<Booking> getAll() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/user/{userId}")
    public List<BookingResponse> getUserBookings(@PathVariable Integer userId) {
        return bookingService.getByUserId(userId);
    }

    @GetMapping("/owner/{ownerId}")
    public List<BookingResponse> getBookingsByOwner(@PathVariable Long ownerId) {
        return bookingService.getBookingsByParkingOwner(ownerId);
    }

    @GetMapping("/byParking/{parkingId}")
    public List<Booking> getBookingsByParking(@PathVariable Long parkingId) {
        return bookingService.getByParkingId(parkingId);
    }

    @GetMapping("/{id}")
    public Booking getById(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        try {
            Booking cancelledBooking = bookingService.cancelBooking(id);
            return ResponseEntity.ok(cancelledBooking);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/confirm/{id}/{VehicleDetected}")
    public String confirmBooking(@PathVariable Long id, @PathVariable String VehicleDetected) {
        try {
            return bookingService.confirmBooking(id, VehicleDetected);
        } catch (IllegalStateException e) {
            return "Error: " + e.getMessage();
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/unmatched-checkin/{id}/{VehicleDetected}")
    public String unMatchedBookingCheckIn(@PathVariable Long id, @PathVariable String VehicleDetected) {
        try {
            return bookingService.unMatchedBookingCheckIn(id, VehicleDetected);
        } catch (IllegalStateException e) {
            return "Error: " + e.getMessage();
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        bookingService.deleteBooking(id);
    }

        @GetMapping("/unmatched-checkins/{ownerId}")
    public List<UnmatchedCheckinResponse> getUnmatchedCheckins(@PathVariable Long ownerId) {
        return bookingService.getUnmatchedCheckinsForOwner(ownerId);
    }

    @PutMapping("/mark-visited")
    public ResponseEntity<?> markUnmatchedCheckinsAsVisited(@RequestBody List<Long> vehicleLogIds) {
        try {
            bookingService.markUnmatchedCheckinsAsVisited(vehicleLogIds);
            return ResponseEntity.ok("Notifications marked as visited");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
