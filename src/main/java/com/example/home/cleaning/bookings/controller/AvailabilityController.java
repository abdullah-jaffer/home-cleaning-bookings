package com.example.home.cleaning.bookings.controller;

import com.example.home.cleaning.bookings.dto.response.AvailabilityResponse;
import com.example.home.cleaning.bookings.service.AvailabilityService;
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
            LocalDate date,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime time,
            @RequestParam(required = false)
            Integer hours) {
        return this.availabilityService.getAvailabilities(date, time, hours);
    }
}
