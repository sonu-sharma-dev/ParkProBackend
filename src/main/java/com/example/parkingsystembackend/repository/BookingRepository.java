package com.example.parkingsystembackend.repository;

import com.example.parkingsystembackend.entity.Booking;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findByParkingId(Long parkingId);
    List<Booking> findByRenterId(Integer renter_id);

    @Query("SELECT b FROM Booking b WHERE b.parkingSlot.id = :slotId " +
            "AND b.startTime < :requestedEnd " +
            "AND b.endTime > :requestedStart")
    List<Booking> findOverlappingBookings(
            @Param("slotId") Long slotId,
            @Param("requestedStart") LocalDateTime requestedStart,
            @Param("requestedEnd") LocalDateTime requestedEnd);

    @Query("SELECT b FROM Booking b WHERE b.parking.owner.id = :ownerId")
    List<Booking> findAllByParkingOwnerId(@Param("ownerId") Long ownerId);

    // Count all bookings for parkings owned by a specific user
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.parking.owner.id = :ownerId")
    int countByOwnerId(@Param("ownerId") Long ownerId);

    // Count today's bookings for owner
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.parking.owner.id = :ownerId AND DATE(b.startTime) = CURRENT_DATE")
    int countTodaysBookingsByOwner(@Param("ownerId") Long ownerId);

    // Sum total earnings
    @Query("SELECT COALESCE(SUM(b.totalPrice), 0) FROM Booking b WHERE b.parking.owner.id = :ownerId")
    double totalEarningsByOwner(@Param("ownerId") Long ownerId);

    // Sum earnings by time window
    @Query("""
        SELECT COALESCE(SUM(b.totalPrice), 0) 
        FROM Booking b 
        WHERE b.parking.owner.id = :ownerId AND b.startTime >= :startTime
    """)
    double earningsSince(@Param("ownerId") Long ownerId, @Param("startTime") LocalDateTime startTime);

}
