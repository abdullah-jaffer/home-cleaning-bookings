package com.example.home.cleaning.bookings.repository.projection;

import java.util.UUID;

public interface CleanerProjection {
    UUID getCleanerId();
    String getCleanerName();
}
