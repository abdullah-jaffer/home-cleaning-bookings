package com.example.home.cleaning.bookings.controller;

import com.example.home.cleaning.bookings.dto.request.BookingRequest;
import com.example.home.cleaning.bookings.service.BookingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("v1/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }


    @PostMapping
    public UUID createBooking(@RequestBody BookingRequest bookingRequest) {
        return this.bookingService.createBooking(bookingRequest);
    }
}
