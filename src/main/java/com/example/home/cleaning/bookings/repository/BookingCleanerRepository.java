package com.example.home.cleaning.bookings.repository;

import com.example.home.cleaning.bookings.entity.BookingCleaner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookingCleanerRepository extends JpaRepository<BookingCleaner, UUID> {
    /**
     * Find all cleaners for given booking
     * @param bookingId
     * @return List of cleaners
     */
    List<BookingCleaner> findByBookingId(UUID bookingId);
}
