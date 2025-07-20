package com.example.parkingsystembackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long bookingId;
    private String status;
    private String slotNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double price;
    private String parkingAddress;
    private Long parkingId;
    private String vehicleNumber;
    private Double longitude;
    private Double latitude;
    private String ownerPhone;
    private String ownerEmail;
    private String ownerName;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private Date createdDate;
}
