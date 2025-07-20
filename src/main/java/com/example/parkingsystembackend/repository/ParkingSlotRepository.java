package com.example.parkingsystembackend.repository;

import com.example.parkingsystembackend.entity.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Integer> {

    List<ParkingSlot> findByParkingId(Long parkingId);
}
