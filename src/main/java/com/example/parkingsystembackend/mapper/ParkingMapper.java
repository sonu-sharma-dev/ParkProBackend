package com.example.parkingsystembackend.mapper;

import com.example.parkingsystembackend.dto.ParkingDTO;
import com.example.parkingsystembackend.entity.Parking;

public class ParkingMapper {
    public static ParkingDTO toDTO(Parking parking) {
        ParkingDTO dto = new ParkingDTO();
        dto.setId(parking.getId());
        dto.setTitle(parking.getTitle());
        dto.setAddress(parking.getAddress());
        dto.setDescription(parking.getDescription());
        dto.setLatitude(parking.getLatitude());
        dto.setLongitude(parking.getLongitude());
        dto.setCharges(parking.getCharges());
        dto.setTotalSlots(parking.getTotalSlots());
        dto.setHasRoof(parking.getHasRoof());
        dto.setCctvAvailable(parking.getCctvAvailable());
        dto.setIsIndoor(parking.getIsIndoor());
        dto.setImageUrl(parking.getImageUrl());
        dto.setOwnerId(parking.getOwner().getId());
        return dto;
    }
}
