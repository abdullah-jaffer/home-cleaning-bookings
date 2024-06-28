package com.example.home.cleaning.bookings.repository;

import com.example.home.cleaning.bookings.repository.projection.CleanerBookingProjection;
import com.example.home.cleaning.bookings.repository.projection.CleanerProjection;
import com.example.home.cleaning.bookings.entity.Cleaner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface CleanerRepository extends JpaRepository<Cleaner, UUID> {

    /**
     * Find all cleaners who have less then 14 hours of booking on given date 22 - 8 = 14 hours
     * @param date
     * @return List of available cleaners
     */
    @Query(
            value = """
                    SELECT cleaners.id as cleanerId, cleaners.name as cleanerName from Cleaners where cleaners.id not in ( \s
                    SELECT cleaners.id AS innerCleanerId
                    FROM cleaners
                    LEFT JOIN booking_cleaners ON cleaners.id  = booking_cleaners.cleaner_id
                    LEFT JOIN bookings ON bookings.id = booking_cleaners.booking_id
                    where DATE(bookings.start_time) = :date
                    GROUP BY cleaners.id
                    HAVING Sum(COALESCE((EXTRACT(EPOCH FROM (bookings.end_time  - bookings.start_time)) / 3600), 0) + 0.5) >= 14)
                    """,
            nativeQuery = true
    )
    List<CleanerProjection> availableCleanersOnGivenDate(LocalDate date);

    /**
     * Find all cleaners who are available on given date
     * @param cleanerIds
     * @param date
     * @return List of available cleaners
     */
    @Query("""
            SELECT cleaner.id AS cleanerId,
            cleaner.name as cleanerName,
            booking.startTime as startTime,
            booking.endTime as endTime
            FROM Cleaner cleaner
            JOIN BookingCleaner bookingCleaner on bookingCleaner.cleanerId = cleaner.id
            JOIN Booking booking on booking.id = bookingCleaner.bookingId
            WHERE cleaner.id IN :cleanerIds
            AND date(booking.startTime) = :date
            AND date(booking.endTime) = :date
            """)
    List<CleanerBookingProjection> findAllCleanersByIds(List<UUID> cleanerIds, LocalDate date);

    /**
     * Find all cleaners who are available on given time
     * @param startTime
     * @param endTime
     * @return List of available cleaners
     */
    @Query("""
            SELECT cleaner.id as cleanerId,
            cleaner.name as cleanerName
            FROM Cleaner cleaner
            WHERE cleaner.id NOT IN (
                  SELECT innerCleaner.id
                  FROM Cleaner innerCleaner
                  JOIN BookingCleaner bookingCleaner ON innerCleaner.id  = bookingCleaner.cleanerId
                  JOIN Booking booking ON booking.id = bookingCleaner.bookingId
                  WHERE
                  ( :endTime > booking.startTime and :startTime < booking.endTime) OR
                  (:startTime < booking.endTime  and :endTime > booking.startTime) OR
                  (booking.startTime BETWEEN :startTime AND :endTime AND booking.endTime BETWEEN :startTime AND :endTime) OR
                  (:startTime BETWEEN booking.startTime AND booking.endTime AND :endTime BETWEEN booking.startTime AND booking.endTime)
                  )
            """)
    List<CleanerProjection> availableCleanersOnGivenTime(LocalDateTime startTime, LocalDateTime endTime);
}
