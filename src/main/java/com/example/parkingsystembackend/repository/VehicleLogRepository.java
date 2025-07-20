package com.example.parkingsystembackend.repository;

import com.example.parkingsystembackend.entity.VehicleLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleLogRepository extends JpaRepository<VehicleLog, Long> {
    @Query("SELECT v.plateNumber as plateNumber, v.detectedAt as detectedAt, v.confidenceScore as confidenceScore, v.matchedBooking.id as matchedBookingId, v.status as matchedBookingStatus " +
            "FROM VehicleLog v WHERE v.matchedBooking.id = :bookingId")
    List<VehicleLogSummary> findSummaryByMatchedBookingId(@Param("bookingId") Long bookingId);

    @Query("SELECT v FROM VehicleLog v WHERE v.status = 'UNMATCHED' AND v.matchedBooking.parking.owner.id = :ownerId AND v.hasVisited = false")
    List<VehicleLog> findUnmatchedByParkingOwnerId(@Param("ownerId") Long ownerId);
}
