package com.example.parkingsystembackend.controller;

import com.example.parkingsystembackend.repository.VehicleLogRepository;
import com.example.parkingsystembackend.repository.VehicleLogSummary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {
    private final VehicleLogRepository vehicleLogRepository;

    public VehicleController(VehicleLogRepository vehicleLogRepository) {
        this.vehicleLogRepository = vehicleLogRepository;
    }

    @GetMapping("/logs/{bookingId}")
    public List<VehicleLogSummary> getVehicleLogs(@PathVariable Long bookingId) {
        return vehicleLogRepository.findSummaryByMatchedBookingId(bookingId);
    }
}
