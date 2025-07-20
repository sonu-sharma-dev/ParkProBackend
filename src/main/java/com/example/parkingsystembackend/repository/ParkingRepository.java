package com.example.parkingsystembackend.repository;

import com.example.parkingsystembackend.entity.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingRepository extends JpaRepository<Parking,Long> {

    List<Parking> findByOwner_Id(Integer ownerId);
    int countByOwnerId(Long ownerId);

    @Query("SELECT COALESCE(SUM(p.totalSlots), 0) FROM Parking p WHERE p.owner.id = :ownerId")
    int sumTotalSlotsByOwner(@Param("ownerId") Long ownerId);
}
