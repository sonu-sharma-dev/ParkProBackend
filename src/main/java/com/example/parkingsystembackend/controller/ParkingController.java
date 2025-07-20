package com.example.parkingsystembackend.controller;

import com.example.parkingsystembackend.dto.ParkingDTO;
import com.example.parkingsystembackend.entity.Parking;
import com.example.parkingsystembackend.service.BookingService;
import com.example.parkingsystembackend.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/parking")
public class ParkingController {
    private final ParkingService parkingService;
    private final BookingService bookingService;

    @PostMapping("/createParking")
    public ParkingDTO createParking(@RequestBody ParkingDTO dto) {
        return parkingService.createParking(dto);
    }

    @PutMapping("/updateParking/{id}")
    public ResponseEntity<ParkingDTO> updateParking(@PathVariable Long id, @RequestBody ParkingDTO dto) {
        return ResponseEntity.ok(parkingService.updateParking(id, dto));
    }

    @GetMapping("/getUserParkings")
    public List<ParkingDTO> getUserParkings(@RequestParam Integer userId){
        return parkingService.getUserParkings(userId);
    }

    @GetMapping("/getAllParking")
    public List<ParkingDTO> getAllParking() {
        return parkingService.getAllParkingSpots();
    }


    @GetMapping("/check/availability")
    public ResponseEntity<?> checkParkingAvailability(
            @RequestParam Long parkingId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        if (end.isBefore(start) || end.isEqual(start)) {
            return ResponseEntity.badRequest().body(Map.of("error", "End time must be after start time"));
        }

        boolean available = bookingService.isParkingAvailable(parkingId, start, end);
        Map<String, Object> response = new HashMap<>();
        response.put("parkingId", parkingId);
        response.put("requestedStart", start);
        response.put("requestedEnd", end);
        response.put("available", available);

        if (!available) {
            Map<String, Object> nearestSlot = bookingService.findNearestAvailableSlot(parkingId, start, end);
            if (nearestSlot != null) {
                response.put("nearestAvailableStart", nearestSlot.get("start"));
                response.put("nearestAvailableEnd", nearestSlot.get("end"));
                response.put("nearestAvailableSlotId", nearestSlot.get("slotId"));
                response.put("nearestAvailableSlotNumber", nearestSlot.get("slotNumber"));
            } else {
                response.put("message", "No availability in the next 7 days");
            }
        }

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ParkingDTO getParkingById(@PathVariable Long id) {
        return parkingService.getParkingById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        parkingService.delete(id);
    }


    @GetMapping("/dashboard/host-summary")
    public ResponseEntity<Map<String, Object>> getHostSummary(@RequestParam Long ownerId) {
        Map<String, Object> response = new HashMap<>();

        int totalParkings = parkingService.countParkingsByOwner(ownerId);
        int totalSlots = parkingService.sumTotalSlotsByOwner(ownerId);
        int totalBookings = bookingService.countBookingsByOwner(ownerId);
        int todaysBookingCount = bookingService.countTodaysBookingsByOwner(ownerId);
        double totalEarnings = bookingService.calculateTotalEarningsByOwner(ownerId);
        double daily = bookingService.calculateEarnings(ownerId, "daily");
        double weekly = bookingService.calculateEarnings(ownerId, "weekly");
        double monthly = bookingService.calculateEarnings(ownerId, "monthly");

        response.put("totalParkings", totalParkings);
        response.put("totalSlots", totalSlots);
        response.put("totalBookings", totalBookings);
        response.put("todaysBookingCount", todaysBookingCount);
        response.put("totalEarnings", totalEarnings);
        response.put("revenue", Map.of(
                "daily", daily,
                "weekly", weekly,
                "monthly", monthly
        ));

        return ResponseEntity.ok(response);
    }
}