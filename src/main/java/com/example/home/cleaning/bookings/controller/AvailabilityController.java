package com.example.home.cleaning.bookings.controller;

import com.example.home.cleaning.bookings.dto.response.AvailabilityResponse;
import com.example.home.cleaning.bookings.service.AvailabilityService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("v1/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService bookingService) {
        this.availabilityService = bookingService;
    }

    @GetMapping
    public List<AvailabilityResponse> getAvailabilities(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "Date in ISO format (yyyy-MM-dd)", required = true, example = "2024-07-01")
            LocalDate date,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            @Parameter(description = "Time in ISO format (HH:mm:ss)", example = "14:30:00")
            LocalTime time,
            @RequestParam(required = false)
            @Parameter(description = "Number of hours", example = "2")
            Integer hours) {
        return this.availabilityService.getAvailabilities(date, time, hours);
    }
}
