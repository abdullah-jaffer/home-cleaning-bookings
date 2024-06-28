package com.example.home.cleaning.bookings.service;

import com.example.home.cleaning.bookings.repository.CleanerRepository;
import com.example.home.cleaning.bookings.repository.projection.CleanerBookingProjection;
import com.example.home.cleaning.bookings.repository.projection.CleanerProjection;
import com.example.home.cleaning.bookings.dto.response.AvailabilityResponse;
import com.example.home.cleaning.bookings.dto.TimeSlot;
import com.example.home.cleaning.bookings.service.strategy.DateBasedStrategy;
import com.example.home.cleaning.bookings.service.strategy.TimeBasedStrategy;
import com.example.home.cleaning.bookings.utils.DateUtils;
import com.example.home.cleaning.bookings.utils.TimeConstants;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.example.home.cleaning.bookings.utils.DateUtils.*;
import static java.util.stream.Collectors.groupingBy;

@Service
public class AvailabilityService {

    private final CleanerRepository cleanerRepository;

    public AvailabilityService(CleanerRepository cleanerRepository) {
        this.cleanerRepository = cleanerRepository;
    }

    /**
     * Get available time slots for cleaners on a given date.
     * This api considers edge cases such as breaks in between appointments.
     * Cleaners with booking and cleaners with no bookings
     * Friday is a holiday for all cleaners
     * Making sure time is between 8 AM and 10 PM
     *
     * @param date  the date for which availability is to be checked
     * @param time  the time for which availability is to be checked
     * @param hours the number of hours for which availability is to be checked
     * @return a list of {@link AvailabilityResponse} containing the available time slots for each cleaner
     */
    public List<AvailabilityResponse> getAvailabilities(LocalDate date, LocalTime time, Integer hours) {
        // If it's Friday, no cleaners are available
        if (DateUtils.isFriday(date)) {
            return List.of();
        }

        if (applyNullValidation(time, hours)) {
            throw new IllegalArgumentException("Both time and hours should be provided or non of them should be provided.");
        }

        // If either time or hours is null, fetch available cleaners for the given date
        if (time == null) {
            var availableCleaners = new DateBasedStrategy().execute(cleanerRepository, date, null, null);
            return fetchAvailableTimeSlots(availableCleaners, date);
        }


        var availableCleaners = new TimeBasedStrategy().execute(cleanerRepository, date, time, hours);
        return fetchAvailableTimeSlots(availableCleaners, date);

    }


    private static boolean applyNullValidation(LocalTime time, Integer hours) {
        return time == null && hours != null || time != null && hours == null;
    }

    /**
     * Fetch available time slots for each cleaner.
     *
     * @param availableCleaners a list of available cleaners
     * @param date              the date for which availability is to be checked
     * @return a list of {@link AvailabilityResponse} containing the available time slots for each cleaner
     */
    private List<AvailabilityResponse> fetchAvailableTimeSlots(List<CleanerProjection> availableCleaners, LocalDate date) {

        var cleanerBookings = cleanerRepository.findAllCleanersByIds(
                availableCleaners.stream().map(CleanerProjection::getCleanerId).toList(), date);

        var bookingsMap = cleanerBookings.stream()
                .collect(groupingBy(CleanerBookingProjection::getCleanerId));

        return availableCleaners.stream().map(cleaner -> {
            var bookings = bookingsMap.getOrDefault(cleaner.getCleanerId(), List.of());

            List<TimeSlot> availableTimes = new ArrayList<>();

            LocalDateTime startTime = date.atTime(TimeConstants.START_HOUR, TimeConstants.START_MINUTE);

            // Collect available time slots
            for (CleanerBookingProjection booking : bookings) {
                if (ChronoUnit.MINUTES.between(startTime, booking.getStartTime()) > TimeConstants.BREAK_IN_MINUTES) {
                    availableTimes.add(new TimeSlot(startTime, booking.getStartTime().minusMinutes(TimeConstants.BREAK_IN_MINUTES)));
                    startTime = booking.getEndTime();
                }
            }

            handleLastAvailableSlot(date, startTime, availableTimes);

            return new AvailabilityResponse(cleaner.getCleanerId(), cleaner.getCleanerName(), availableTimes);

        }).toList();
    }

    private static void handleLastAvailableSlot(LocalDate date, LocalDateTime startTime, List<TimeSlot> availableTimes) {
        // If no booking is found that means cleaner is available from start till the end of the day
        if (NoBookingsFound(date, startTime)) {
            availableTimes.add(new TimeSlot(startTime, date.atTime(TimeConstants.END_HOUR, TimeConstants.END_MINUTE)));
        } else if (ChronoUnit.MINUTES.between(startTime, date.atTime(TimeConstants.END_HOUR, TimeConstants.END_MINUTE)) > TimeConstants.BREAK_IN_MINUTES) {
            // Add the final available slot excluding the 30 minutes break
            availableTimes.add(new TimeSlot(startTime.plusMinutes(TimeConstants.BREAK_IN_MINUTES), date.atTime(TimeConstants.END_HOUR, TimeConstants.END_MINUTE)));
        }
    }

    private static boolean NoBookingsFound(LocalDate date, LocalDateTime startTime) {
        return startTime.isEqual(date.atTime(TimeConstants.START_HOUR, TimeConstants.START_MINUTE));
    }
}
