package com.example.parkingsystembackend.mapper;

import com.example.parkingsystembackend.dto.UnmatchedCheckinResponse;
import com.example.parkingsystembackend.entity.VehicleLog;

public class VehicleLogMapper {
    public static UnmatchedCheckinResponse toUnmatchedCheckinResponse(VehicleLog vehicleLog) {
        return new UnmatchedCheckinResponse(
                vehicleLog.getId(),
                vehicleLog.getPlateNumber(),
                vehicleLog.getDetectedAt(),
                vehicleLog.getParking().getTitle(),
                vehicleLog.getParking().getAddress(),
                vehicleLog.getMatchedBooking().getId(),
                vehicleLog.getMatchedBooking().getVehicleNumber(),
                vehicleLog.getMatchedBooking().getRenter().getName(),
                vehicleLog.getMatchedBooking().getRenter().getPhoneNumber(),
                vehicleLog.getMatchedBooking().getStartTime(),
                vehicleLog.getMatchedBooking().getEndTime()
        );
    }
}
