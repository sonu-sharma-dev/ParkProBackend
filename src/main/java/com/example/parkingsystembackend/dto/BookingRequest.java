package com.example.parkingsystembackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    private Integer userId;
    private Long parkingId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double totalPrice;
    private String vehicleNumber;
}
