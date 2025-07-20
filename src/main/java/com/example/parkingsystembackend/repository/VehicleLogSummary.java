package com.example.parkingsystembackend.repository;

import java.time.LocalDateTime;

public interface VehicleLogSummary {
    String getPlateNumber();
    LocalDateTime getDetectedAt();
    Double getConfidenceScore();
    Long getMatchedBookingId();
    String getMatchedBookingStatus();
}
