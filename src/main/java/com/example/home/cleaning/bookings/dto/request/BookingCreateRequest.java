package com.example.home.cleaning.bookings.dto.request;

import com.example.home.cleaning.bookings.dto.TimeSlot;

import java.util.List;
import java.util.UUID;

public record BookingCreateRequest(UUID customerId, TimeSlot requestedSchedule, List<UUID> selectedCleanerIds) {
}
