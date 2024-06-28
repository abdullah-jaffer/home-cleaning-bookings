package com.example.home.cleaning.bookings.service.strategy;

import com.example.home.cleaning.bookings.repository.CleanerRepository;
import com.example.home.cleaning.bookings.repository.projection.CleanerProjection;
import com.example.home.cleaning.bookings.utils.DateUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.example.home.cleaning.bookings.utils.DateUtils.applyTimeSanitization;

public class TimeBasedStrategy implements AvailabilityStrategy {
    @Override
    public List<CleanerProjection> execute(
            CleanerRepository cleanerRepository,
            LocalDate date,
            LocalTime time,
            Integer hours) {

        // This function will throw an exception if the time is not between the start and end time
        // It will also pad 30 mins before and after the time to account for breaks
        DateUtils.SanitizedTime result = applyTimeSanitization(date, time, hours);

        return cleanerRepository.availableCleanersOnGivenTime(result.startDateTime(), result.endDateTime());
    }
}
