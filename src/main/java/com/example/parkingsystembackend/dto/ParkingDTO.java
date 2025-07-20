package com.example.parkingsystembackend.dto;

import lombok.Data;

@Data
public class ParkingDTO {
    private Long id;
    private String title;
    private String address;
    private String description;
    private Double latitude;
    private Double longitude;
    private Double charges;         // Charges per hour
    private Integer totalSlots;
    private Boolean hasRoof;
    private Boolean cctvAvailable;
    private Boolean isIndoor;
    private String imageUrl;        // base64 string
    private Integer ownerId;           // ID of user/owner creating parking
}
