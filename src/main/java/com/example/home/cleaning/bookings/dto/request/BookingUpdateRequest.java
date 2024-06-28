package com.example.home.cleaning.bookings.dto.request;

import com.example.home.cleaning.bookings.dto.TimeSlot;

public record BookingUpdateRequest(TimeSlot requestedSchedule) {
}
