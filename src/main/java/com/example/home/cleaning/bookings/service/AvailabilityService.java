package com.example.home.cleaning.bookings.service;

import com.example.home.cleaning.bookings.repository.CleanerRepository;
import com.example.home.cleaning.bookings.repository.projection.CleanerBookingProjection;
import com.example.home.cleaning.bookings.repository.projection.CleanerProjection;
import com.example.home.cleaning.bookings.dto.response.AvailabilityResponse;
import com.example.home.cleaning.bookings.dto.TimeSlot;
import com.example.home.cleaning.bookings.utils.DateUtils;
import com.example.home.cleaning.bookings.utils.TimeConstants;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.example.home.cleaning.bookings.utils.DateUtils.addBreak;
import static com.example.home.cleaning.bookings.utils.DateUtils.removeBreak;
import static java.util.stream.Collectors.groupingBy;

@Service
public class AvailabilityService {

    private final CleanerRepository cleanerRepository;

    public AvailabilityService(CleanerRepository cleanerRepository) {
        this.cleanerRepository = cleanerRepository;
    }

    public List<AvailabilityResponse> getAvailabilities(LocalDate date, LocalTime time, Integer hours) {
        if (DateUtils.isFriday(date)) {
            return List.of();
        }

        if (applyNullValidation(time, hours)) {
            throw new IllegalArgumentException("Both time and hours should be provided or non of them should be provided.");
        }

        if (time == null) {
            var availableCleaners = cleanerRepository.availableCleanersOnGivenDate(date);
            return fetchAvailableTimeSlots(availableCleaners, date);
        }


        var startDateTime = LocalDateTime.of(date, time);
        var endDateTime = startDateTime.plusHours(hours);

        startDateTime = addBreak(startDateTime);
        endDateTime = removeBreak(endDateTime);

        if (DateUtils.checkBoundaryDate(startDateTime, endDateTime)) {
            throw new IllegalArgumentException(String.format("Time should be between %s:00 and %s:00", TimeConstants.START_HOUR, TimeConstants.END_HOUR)
            );
        }

        var availableCleaners = cleanerRepository.availableCleanersOnGivenTime(startDateTime, endDateTime);

        return fetchAvailableTimeSlots(availableCleaners, date);

    }

    private static boolean applyNullValidation(LocalTime time, Integer hours) {
        return time == null && hours != null || time != null && hours == null;
    }

    private List<AvailabilityResponse> fetchAvailableTimeSlots(List<CleanerProjection> availableCleaners, LocalDate date) {

        var cleanerBookings = cleanerRepository.findAllCleanersByIds(
                availableCleaners.stream().map(CleanerProjection::getCleanerId).toList(), date);

        var bookingsMap = cleanerBookings.stream()
                .collect(groupingBy(CleanerBookingProjection::getCleanerId));

        return availableCleaners.stream().map(cleaner -> {
            var bookings = bookingsMap.getOrDefault(cleaner.getCleanerId(), List.of());

            List<TimeSlot> availableTimes = new ArrayList<>();

            LocalDateTime startTime = date.atTime(TimeConstants.START_HOUR, TimeConstants.START_MINUTE);

            for (CleanerBookingProjection booking : bookings) {
                if (ChronoUnit.MINUTES.between(startTime, booking.getStartTime()) > TimeConstants.BREAK_IN_MINUTES) {
                    availableTimes.add(new TimeSlot(startTime, booking.getStartTime().minusMinutes(TimeConstants.BREAK_IN_MINUTES)));
                    startTime = booking.getEndTime();
                }
            }

            if (startTime.isEqual(date.atTime(TimeConstants.START_HOUR, TimeConstants.START_MINUTE))) {
                availableTimes.add(new TimeSlot(startTime, date.atTime(TimeConstants.END_HOUR, TimeConstants.END_MINUTE)));
            } else if (ChronoUnit.MINUTES.between(startTime, date.atTime(TimeConstants.END_HOUR, TimeConstants.END_MINUTE)) > TimeConstants.BREAK_IN_MINUTES) {
                availableTimes.add(new TimeSlot(startTime.plusMinutes(TimeConstants.BREAK_IN_MINUTES), date.atTime(TimeConstants.END_HOUR, TimeConstants.END_MINUTE)));
            }

            return new AvailabilityResponse(cleaner.getCleanerId(), cleaner.getCleanerName(), availableTimes);

        }).toList();
    }
}
