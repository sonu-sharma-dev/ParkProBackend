package com.example.parkingsystembackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parking_slots")
public class ParkingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // e.g. "A1", "1", "2"
    private String slotNumber;

    // to disable slot if needed
    private Boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "parking_id")
    private Parking parking;
}
