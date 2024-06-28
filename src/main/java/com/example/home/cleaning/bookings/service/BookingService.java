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

    @Transactional
    public UUID createBooking(BookingCreateRequest bookingRequest) {

        if(vehicleCleanerRepository.findByCleanerIds(bookingRequest.selectedCleanerIds()).size() > 1) {
            throw new IllegalArgumentException("Selected cleaners do not have the same vehicle");
        }

        if(!bookingRepository.checkBookingForCleaners(
                bookingRequest.selectedCleanerIds(),
                bookingRequest.requestedSchedule().startTime(),
                bookingRequest.requestedSchedule().endTime()).isEmpty()) {
            throw new IllegalArgumentException("Selected cleaners are not available for the requested schedule");
        }


        Booking booking = new Booking(UUID.randomUUID(),
                bookingRequest.requestedSchedule().startTime(),
                bookingRequest.requestedSchedule().endTime(),
                bookingRequest.customerId());

        bookingRepository.save(booking);

        bookingCleanerRepository.saveAll(bookingRequest.selectedCleanerIds().stream().map(cleanerId ->
                new BookingCleaner(UUID.randomUUID(), cleanerId, booking.getId())).toList());

        return booking.getId();
    }

    @Transactional
    public void updateBooking(UUID bookingId, BookingUpdateRequest bookingUpdateRequest) {
        var cleanerIds = bookingCleanerRepository.findByBookingId(bookingId)
                .stream().map(BookingCleaner::getCleanerId).toList();

        if(!bookingRepository.checkBookingForCleaners(
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
