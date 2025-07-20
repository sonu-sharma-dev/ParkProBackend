package com.example.parkingsystembackend.service;

import com.example.parkingsystembackend.dto.ParkingDTO;
import com.example.parkingsystembackend.entity.Parking;
import com.example.parkingsystembackend.entity.ParkingSlot;
import com.example.parkingsystembackend.mapper.ParkingMapper;
import com.example.parkingsystembackend.repository.ParkingRepository;
import com.example.parkingsystembackend.repository.ParkingSlotRepository;
import com.example.parkingsystembackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParkingService {
    private final ParkingRepository parkingRepository;
    private final UserRepository userRepository;
    private final ParkingSlotRepository parkingSlotRepository;

    public ParkingDTO createParking(ParkingDTO dto) {
        Parking parking = new Parking();

        parking.setTitle(dto.getTitle());
        parking.setAddress(dto.getAddress());
        parking.setDescription(dto.getDescription());
        parking.setLatitude(dto.getLatitude());
        parking.setLongitude(dto.getLongitude());
        parking.setCharges(dto.getCharges());
        parking.setTotalSlots(dto.getTotalSlots());
        parking.setAvailableSlots(dto.getTotalSlots());  // Initially all slots available
        parking.setHasRoof(dto.getHasRoof());
        parking.setCctvAvailable(dto.getCctvAvailable());
        parking.setIsIndoor(dto.getIsIndoor());
        parking.setImageUrl(dto.getImageUrl());
        parking.setIsActive(true);

        // Find user by ownerId
        userRepository.findById(dto.getOwnerId()).ifPresent(parking::setOwner);

         Parking savedParking = parkingRepository.save(parking);

        // Create slots for this parking
        List<ParkingSlot> slots = new ArrayList<>();
        for (int i = 1; i <= dto.getTotalSlots(); i++) {
            ParkingSlot slot = new ParkingSlot();
            slot.setSlotNumber(String.valueOf(i));
            slot.setParking(savedParking);
            slot.setIsActive(true);
            slots.add(slot);
        }
        parkingSlotRepository.saveAll(slots);

        return ParkingMapper.toDTO(savedParking);
    }

    public ParkingDTO updateParking(Long id, ParkingDTO dto) {
        Parking parking = parkingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking not found with id: " + id));

        parking.setTitle(dto.getTitle());
        parking.setAddress(dto.getAddress());
        parking.setDescription(dto.getDescription());
        parking.setLatitude(dto.getLatitude());
        parking.setLongitude(dto.getLongitude());
        parking.setCharges(dto.getCharges());
        parking.setTotalSlots(dto.getTotalSlots());

        // Adjust available slots if total slots change (optional logic)
        int usedSlots = parking.getTotalSlots() - parking.getAvailableSlots();
        parking.setAvailableSlots(Math.max(0, dto.getTotalSlots() - usedSlots));

        parking.setHasRoof(dto.getHasRoof());
        parking.setCctvAvailable(dto.getCctvAvailable());
        parking.setIsIndoor(dto.getIsIndoor());
        parking.setImageUrl(dto.getImageUrl());


        return ParkingMapper.toDTO(parkingRepository.save(parking));
    }

    public List<ParkingDTO> getAllParkingSpots() {
        List<Parking> parkings = parkingRepository.findAll();
        return parkings.stream()
                .map(ParkingMapper::toDTO)
                .collect(Collectors.toList());
    }


    public ParkingDTO getParkingById(Long id) {
        return  ParkingMapper.toDTO(parkingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking spot not found")));
    }

    public void delete(Long id) {
        parkingRepository.deleteById(id);
    }

    public List<ParkingDTO> getUserParkings(Integer userId) {
        return parkingRepository.findByOwner_Id(userId)
                .stream().map(ParkingMapper::toDTO)
                .collect(Collectors.toList());
    }

    public int countParkingsByOwner(Long ownerId) {
        return parkingRepository.countByOwnerId(ownerId);
    }

    public int sumTotalSlotsByOwner(Long ownerId) {
        return parkingRepository.sumTotalSlotsByOwner(ownerId);
    }
}
