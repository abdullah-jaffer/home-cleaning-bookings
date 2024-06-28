package com.example.home.cleaning.bookings.service;

import com.example.home.cleaning.bookings.dto.request.BookingCreateRequest;
import com.example.home.cleaning.bookings.dto.request.BookingUpdateRequest;
import com.example.home.cleaning.bookings.entity.Booking;
import com.example.home.cleaning.bookings.entity.BookingCleaner;
import com.example.home.cleaning.bookings.repository.BookingCleanerRepository;
import com.example.home.cleaning.bookings.repository.BookingRepository;
import com.example.home.cleaning.bookings.repository.VehicleCleanerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final VehicleCleanerRepository vehicleCleanerRepository;
    private final BookingCleanerRepository bookingCleanerRepository;

    public BookingService(
            BookingRepository bookingRepository,
            VehicleCleanerRepository vehicleCleanerRepository,
            BookingCleanerRepository bookingCleanerRepository) {
        this.bookingRepository = bookingRepository;
        this.vehicleCleanerRepository = vehicleCleanerRepository;
        this.bookingCleanerRepository = bookingCleanerRepository;
    }

    /**
     * Create a booking
     * Check that the cleaners belong to the same vehicle
     * Check that the cleaners are available for the requested schedule
     * since it is possible someone else booked them in between
     *
     * @param bookingRequest booking request
     * @return booking id
     */
    @Transactional
    public UUID createBooking(BookingCreateRequest bookingRequest) {

        if (vehicleCleanerRepository.findByCleanerIds(bookingRequest.selectedCleanerIds()).size() > 1) {
            throw new IllegalArgumentException("Selected cleaners do not have the same vehicle");
        }

        if (!bookingRepository.checkBookingForCleaners(
                bookingRequest.selectedCleanerIds(),
                bookingRequest.requestedSchedule().startTime(),
                bookingRequest.requestedSchedule().endTime()).isEmpty()) {
            throw new IllegalArgumentException("Selected cleaners are not available for the requested schedule");
        }


        Booking booking = new Booking.Builder().id(UUID.randomUUID())
                .startTime(bookingRequest.requestedSchedule().startTime())
                .endTime(bookingRequest.requestedSchedule().endTime())
                .customerId(bookingRequest.customerId())
                .build();

        bookingRepository.save(booking);

        bookingCleanerRepository.saveAll(bookingRequest.selectedCleanerIds().stream().map(cleanerId ->
                new BookingCleaner.Builder().withId(UUID.randomUUID())
                        .withCleanerId(cleanerId)
                        .withBookingId(booking.getId())
                        .build()).toList());

        return booking.getId();
    }

    /**
     * Update a booking
     * Check that the cleaners are available for the requested schedule
     * since it is possible someone else booked them in between
     *
     * @param bookingId            booking id
     * @param bookingUpdateRequest booking update request
     */
    @Transactional
    public void updateBooking(UUID bookingId, BookingUpdateRequest bookingUpdateRequest) {
        var cleanerIds = bookingCleanerRepository.findByBookingId(bookingId)
                .stream().map(BookingCleaner::getCleanerId).toList();

        if (!bookingRepository.checkBookingForCleaners(
                cleanerIds,
                bookingUpdateRequest.requestedSchedule().startTime(),
                bookingUpdateRequest.requestedSchedule().endTime()).isEmpty()) {
            throw new IllegalArgumentException("Selected cleaners are not available for the requested schedule");
        }

        bookingRepository.updateAppointment(bookingId,
                bookingUpdateRequest.requestedSchedule().startTime(),
                bookingUpdateRequest.requestedSchedule().endTime());

    }
}
