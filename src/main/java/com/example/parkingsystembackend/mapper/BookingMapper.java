package com.example.parkingsystembackend.mapper;

import com.example.parkingsystembackend.dto.BookingResponse;
import com.example.parkingsystembackend.entity.Booking;

public class BookingMapper {
    public static BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getStatus().name(),
                booking.getParkingSlot().getSlotNumber(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getTotalPrice(),
                booking.getParking().getAddress(),
                booking.getParking().getId(),
                booking.getVehicleNumber(),
                booking.getParking().getLongitude(),
                booking.getParking().getLatitude(),
                booking.getParking().getOwner().getPhoneNumber(),
                booking.getParking().getOwner().getEmail(),
                booking.getParking().getOwner().getName(),
                booking.getRenter().getName(),
                booking.getRenter().getPhoneNumber(),
                booking.getRenter().getEmail(),
                booking.getCreatedDate()
        );
    }
}

