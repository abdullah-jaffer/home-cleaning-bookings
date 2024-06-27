package com.example.home.cleaning.bookings.repository;

import com.example.home.cleaning.bookings.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
}
