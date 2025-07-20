package com.example.parkingsystembackend.service;

import com.example.parkingsystembackend.dto.BookingRequest;
import com.example.parkingsystembackend.dto.BookingResponse;
import com.example.parkingsystembackend.dto.UnmatchedCheckinResponse;
import com.example.parkingsystembackend.entity.*;
import com.example.parkingsystembackend.mapper.BookingMapper;
import com.example.parkingsystembackend.mapper.VehicleLogMapper;
import com.example.parkingsystembackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final UserRepository userRepository;
    private final ParkingRepository parkingRepository;
    private final VehicleLogRepository vehicleLogRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();

    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).
                 orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    public List<Booking> getByParkingId(Long parkingId) {
        return bookingRepository.findByParkingId(parkingId);
    }

    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        // Validate user and parking exist
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Parking parking = parkingRepository.findById(request.getParkingId())
                .orElseThrow(() -> new RuntimeException("Parking not found"));

        // Find free parking slot for requested time
        List<ParkingSlot> slots = parkingSlotRepository.findByParkingId(parking.getId());
        ParkingSlot freeSlot = null;

        for (ParkingSlot slot : slots) {
            boolean isFree = isSlotFree(slot, request.getStartTime(), request.getEndTime());
            if (isFree) {
                freeSlot = slot;
                break;
            }
        }

        if (freeSlot == null) {
            throw new RuntimeException("No available parking slots for the selected time");
        }

        // Create booking
        Booking booking = new Booking();
        booking.setRenter(user);
        booking.setParking(parking);
        booking.setParkingSlot(freeSlot);
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setTotalPrice(request.getTotalPrice());
        booking.setVehicleNumber(request.getVehicleNumber());
        booking.setStatus(Booking.Status.CONFIRMED);
        booking.setCreatedDate(new Date());

        booking = bookingRepository.save(booking);

        return BookingMapper.toResponse(booking);

    }

    public Booking cancelBooking(Long bookingId) throws BadRequestException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BadRequestException("Booking not found with id " + bookingId));

        if (booking.getStatus() == Booking.Status.CANCELLED) {
            throw new IllegalStateException("Booking is already cancelled");
        }

        // Update status to CANCELLED
        booking.setStatus(Booking.Status.CANCELLED);
        return bookingRepository.save(booking);
    }


    private boolean isSlotFree(ParkingSlot slot, LocalDateTime start, LocalDateTime end) {
        // Check if slot has any bookings overlapping requested time
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(slot.getId(), start, end);
        return overlappingBookings.isEmpty();
    }

    public Map<String, Object> findNearestAvailableSlot(Long parkingId, LocalDateTime requestedStart, LocalDateTime requestedEnd) {
        List<ParkingSlot> slots = parkingSlotRepository.findByParkingId(parkingId);
        Duration duration = Duration.between(requestedStart, requestedEnd);
        LocalDateTime maxSearch = requestedStart.plusDays(7);

        LocalDateTime earliestStart = null;
        LocalDateTime earliestEnd = null;
        ParkingSlot bestSlot = null;

        for (ParkingSlot slot : slots) {
            LocalDateTime candidateStart = requestedStart;
            while (candidateStart.isBefore(maxSearch)) {
                LocalDateTime candidateEnd = candidateStart.plus(duration);
                boolean isFree = bookingRepository.findOverlappingBookings(slot.getId(), candidateStart, candidateEnd).isEmpty();

                if (isFree) {
                    if (earliestStart == null || candidateStart.isBefore(earliestStart)) {
                        earliestStart = candidateStart;
                        earliestEnd = candidateEnd;
                        bestSlot = slot;
                    }
                    break; // Found earliest for this slot, check next slot
                }
                candidateStart = candidateStart.plusMinutes(30);
            }
        }

        if (bestSlot != null) {
            return Map.of(
                    "start", earliestStart,
                    "end", earliestEnd,
                    "slotId", bestSlot.getId(),
                    "slotNumber", bestSlot.getSlotNumber());
        }

        return null; // No availability in next 7 days
    }

    public boolean isParkingAvailable(Long parkingId, LocalDateTime start, LocalDateTime end) {
        List<ParkingSlot> slots = parkingSlotRepository.findByParkingId(parkingId);

        for (ParkingSlot slot : slots) {
            boolean isFree = bookingRepository.findOverlappingBookings(slot.getId(), start, end).isEmpty();
            if (isFree) {
                return true;
            }
        }
        return false;
    }

    public List<BookingResponse> getByUserId(Integer userId) {
        return bookingRepository.findByRenterId(userId).stream()
                .map(BookingMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getBookingsByParkingOwner(Long ownerId) {
        return bookingRepository.findAllByParkingOwnerId(ownerId)
                .stream().map(BookingMapper::toResponse).collect(Collectors.toList());
    }

    public String confirmBooking(Long id, String VehicleDetected) throws BadRequestException {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Booking not found with id " + id));

        VehicleLog vehicleLog = new VehicleLog();
        vehicleLog.setMatchedBooking(booking);
        vehicleLog.setParking(booking.getParking());
        vehicleLog.setPlateNumber(VehicleDetected);
        vehicleLog.setDetectedAt(LocalDateTime.now());
        vehicleLog.setStatus(VehicleLog.MatchStatus.MATCHED);
        vehicleLogRepository.save(vehicleLog);

        booking.setStatus(Booking.Status.COMPLETED);

         bookingRepository.save(booking);
        return "Success";
    }

    public String unMatchedBookingCheckIn(Long id, String VehicleDetected) throws BadRequestException {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Booking not found with id " + id));

        VehicleLog vehicleLog = new VehicleLog();
        vehicleLog.setMatchedBooking(booking);
        vehicleLog.setParking(booking.getParking());
        vehicleLog.setPlateNumber(VehicleDetected);
        vehicleLog.setDetectedAt(LocalDateTime.now());
        vehicleLog.setStatus(VehicleLog.MatchStatus.UNMATCHED);
        vehicleLog.setHasVisited(false);
        vehicleLogRepository.save(vehicleLog);

        return "Success";
    }


    public int countBookingsByOwner(Long ownerId) {
        return bookingRepository.countByOwnerId(ownerId);
    }

    public int countTodaysBookingsByOwner(Long ownerId) {
        return bookingRepository.countTodaysBookingsByOwner(ownerId);
    }

    public double calculateTotalEarningsByOwner(Long ownerId) {
        return bookingRepository.totalEarningsByOwner(ownerId);
    }

    public double calculateEarnings(Long ownerId, String type) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = switch (type.toLowerCase()) {
            case "daily" -> now.toLocalDate().atStartOfDay();
            case "weekly" -> now.minusDays(7);
            case "monthly" -> now.minusDays(30);
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };

        return bookingRepository.earningsSince(ownerId, startTime);
    }

    public List<UnmatchedCheckinResponse> getUnmatchedCheckinsForOwner(Long ownerId) {
        return vehicleLogRepository.findUnmatchedByParkingOwnerId(ownerId)
                .stream()
                .map(VehicleLogMapper::toUnmatchedCheckinResponse)
                .collect(Collectors.toList());
    }

    public void markUnmatchedCheckinsAsVisited(List<Long> vehicleLogIds) throws BadRequestException {
        List<VehicleLog> vehicleLogs = vehicleLogRepository.findAllById(vehicleLogIds);

        if (vehicleLogs.size() != vehicleLogIds.size()) {
            throw new BadRequestException("One or more vehicle logs not found");
        }

        vehicleLogs.forEach(log -> log.setHasVisited(true));
        vehicleLogRepository.saveAll(vehicleLogs);
    }

}
