package com.example.home.cleaning.bookings.service.strategy;

import com.example.home.cleaning.bookings.repository.CleanerRepository;
import com.example.home.cleaning.bookings.repository.projection.CleanerProjection;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DateBasedStrategy implements AvailabilityStrategy {
    @Override
    public List<CleanerProjection> execute(
            CleanerRepository cleanerRepository,
            LocalDate date,
            LocalTime time,
            Integer hours){
        return cleanerRepository.availableCleanersOnGivenDate(date);
    }
}
