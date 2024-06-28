package com.example.home.cleaning.bookings.controller;

import com.example.home.cleaning.bookings.dto.request.BookingCreateRequest;
import com.example.home.cleaning.bookings.dto.request.BookingUpdateRequest;
import com.example.home.cleaning.bookings.service.BookingService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("v1/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }


    @PostMapping
    public UUID createBooking(@RequestBody BookingCreateRequest bookingCreateRequest) {
        return this.bookingService.createBooking(bookingCreateRequest);
    }

    @PutMapping("/{bookingId}")
    public void updateBooking(@PathVariable UUID bookingId,
                              @RequestBody BookingUpdateRequest bookingUpdateRequest) {
        this.bookingService.updateBooking(bookingId, bookingUpdateRequest);
    }
}
