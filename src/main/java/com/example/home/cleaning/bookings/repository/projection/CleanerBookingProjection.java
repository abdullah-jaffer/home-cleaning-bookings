package com.example.home.cleaning.bookings.repository.projection;

import java.time.LocalDateTime;
import java.util.UUID;

public interface CleanerBookingProjection {
    UUID getCleanerId();
    String getCleanerName();
    LocalDateTime getStartTime();
    LocalDateTime getEndTime();
}
