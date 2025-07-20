package com.example.parkingsystembackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parking_timings")
public class ParkingTiming {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parking_id")
    private Parking parking;

    private LocalTime availableFrom;
    private LocalTime availableTo;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
}
