package com.example.home.cleaning.bookings.dto;

import java.time.LocalDateTime;

public record TimeSlot(LocalDateTime startTime, LocalDateTime endTime) {
}
