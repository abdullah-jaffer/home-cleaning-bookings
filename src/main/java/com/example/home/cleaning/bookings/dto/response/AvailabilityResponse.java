package com.example.home.cleaning.bookings.dto.response;

import com.example.home.cleaning.bookings.dto.TimeSlot;

import java.util.List;
import java.util.UUID;

public record AvailabilityResponse(UUID cleanerId, String name, List<TimeSlot> availableTimes) {
}
