package com.example.home.cleaning.bookings.dto;

import java.util.List;
import java.util.UUID;

public record AvailabilityResponse(UUID cleanerId, String name, List<TimeSlot> availableTimes) {
}
