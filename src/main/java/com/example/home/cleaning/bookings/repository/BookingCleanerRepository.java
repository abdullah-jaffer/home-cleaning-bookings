package com.example.home.cleaning.bookings.repository;

import com.example.home.cleaning.bookings.entity.BookingCleaner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookingCleanerRepository extends JpaRepository<BookingCleaner, UUID> {
}
