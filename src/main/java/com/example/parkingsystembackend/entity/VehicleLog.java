package com.example.parkingsystembackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vehicle_logs")
public class VehicleLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String plateNumber;
    private String imageUrl;
    private LocalDateTime detectedAt;

    @Enumerated(EnumType.STRING)
    private Direction direction;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    private Double confidenceScore;

    @ManyToOne
    @JoinColumn(name = "parking_id")
    private Parking parking;

    @ManyToOne
    @JoinColumn(name = "matched_booking_id", nullable = true)
    private Booking matchedBooking;

    @Column(name = "has_visited",nullable = true)
    private Boolean hasVisited;

    public enum Direction {
        ENTRY, EXIT
    }

    public enum MatchStatus {
        MATCHED, UNMATCHED, PENDING_REVIEW
    }
}
