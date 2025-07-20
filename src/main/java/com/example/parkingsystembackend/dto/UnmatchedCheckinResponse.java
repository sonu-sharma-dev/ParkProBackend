package com.example.parkingsystembackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnmatchedCheckinResponse {
    private Long vehicleLogId;
    private String plateNumber;
    private LocalDateTime detectedAt;
    private String parkingName;
    private String parkingAddress;
    private Long bookingId;
    private String expectedVehicleNumber;
    private String customerName;
    private String customerPhone;
    private LocalDateTime bookingStartTime;
    private LocalDateTime bookingEndTime;
}