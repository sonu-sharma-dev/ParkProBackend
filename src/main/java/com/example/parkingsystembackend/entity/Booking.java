package com.example.parkingsystembackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User renter;

    @ManyToOne
    @JoinColumn(name = "parking_slot_id")
    private ParkingSlot parkingSlot;

    @ManyToOne
    @JoinColumn(name = "parking_id")
    private Parking parking;

    private String vehicleNumber;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double totalPrice;

    private Date createdDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        CONFIRMED, CANCELLED, COMPLETED
    }
}

