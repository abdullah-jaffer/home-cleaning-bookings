package com.example.home.cleaning.bookings.repository;

import com.example.home.cleaning.bookings.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {


    @Query("""
            SELECT booking
            FROM Booking booking JOIN BookingCleaner bookingCleaner
            ON booking.id = bookingCleaner.bookingId
            WHERE bookingCleaner.cleanerId IN :cleanerIds
            AND (
            (:endTime > booking.startTime AND :startTime < booking.endTime) OR
            (:endTime > booking.startTime AND :startTime < booking.endTime) OR
            (booking.startTime BETWEEN :startTime AND :endTime AND booking.endTime BETWEEN :startTime AND :endTime) OR
            (:startTime BETWEEN booking.startTime AND booking.endTime AND :endTime BETWEEN booking.startTime AND booking.endTime)
            )
            """)
    List<Booking> checkBookingForCleaners(List<UUID> cleanerIds, LocalDateTime startTime, LocalDateTime endTime);

    @Modifying
    @Query("""
            UPDATE Booking booking
            SET booking.startTime = :startTime, booking.endTime = :endTime
            WHERE booking.id = :bookingId
            """)
    void updateAppointment(UUID bookingId, LocalDateTime startTime, LocalDateTime endTime);
}
