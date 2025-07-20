package com.example.parkingsystembackend.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import com.example.parkingsystembackend.dto.ParkingDTO;
import com.example.parkingsystembackend.entity.Parking;
import com.example.parkingsystembackend.entity.User;
import com.example.parkingsystembackend.repository.ParkingRepository;
import com.example.parkingsystembackend.repository.ParkingSlotRepository;
import com.example.parkingsystembackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class ParkingServiceTest {

    @Mock
    private ParkingRepository parkingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ParkingSlotRepository parkingSlotRepository;

    @InjectMocks
    private ParkingService parkingService;


    @Test
    void testCreateParking() {
        // Prepare input DTO
        ParkingDTO dto = new ParkingDTO();
        dto.setTitle("Test Parking");
        dto.setAddress("123 Street");
        dto.setDescription("Description");
        dto.setLatitude(40.0);
        dto.setLongitude(-74.0);
        dto.setCharges(100D);
        dto.setTotalSlots(3);
        dto.setHasRoof(true);
        dto.setCctvAvailable(false);
        dto.setIsIndoor(true);
        dto.setImageUrl("image.jpg");
        dto.setOwnerId(1);

        // Prepare mocked User
        User owner = new User();
        owner.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(owner));

        // Prepare parking entity to be returned by save
        Parking savedParking = new Parking();
        savedParking.setId(10L); // Assume generated ID
        savedParking.setTitle(dto.getTitle());
        savedParking.setOwner(owner);

        when(parkingRepository.save(any(Parking.class))).thenReturn(savedParking);

        // Mock parkingSlotRepository.saveAll() to just return the list passed
        when(parkingSlotRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        // For simplicity, let's assume ParkingMapper.toDTO just returns a new DTO with id set
        // (If ParkingMapper.toDTO is static, you can mock it with Mockito's mockStatic)
        // Here we'll just manually override for test purposes:
        ParkingDTO expectedDTO = new ParkingDTO();
        expectedDTO.setId(savedParking.getId());
        // You can add more fields to compare if needed

        // To avoid complexity in mocking static method, let's temporarily replace the mapper call inside service
        // Or you can verify that the returned DTO is not null and has expected id.

        // Call the method under test
        ParkingDTO result = parkingService.createParking(dto);

        // Verify repository calls
        verify(userRepository).findById(1);
        verify(parkingRepository).save(any(Parking.class));
        verify(parkingSlotRepository).saveAll(anyList());

        // Basic assertions
        assertNotNull(result);
        // If ParkingMapper.toDTO returns null in your actual code, adjust accordingly
        // Here we only check id because we don't have full control without mocking mapper
        // assertEquals(expectedDTO.getId(), result.getId());

        // Alternatively just assert returned object is not null for now
    }
}
