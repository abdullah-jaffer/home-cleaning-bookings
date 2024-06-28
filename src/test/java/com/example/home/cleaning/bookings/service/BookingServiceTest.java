package com.example.home.cleaning.bookings.service;

import com.example.home.cleaning.bookings.dto.TimeSlot;
import com.example.home.cleaning.bookings.dto.request.BookingCreateRequest;
import com.example.home.cleaning.bookings.dto.request.BookingUpdateRequest;
import com.example.home.cleaning.bookings.entity.Booking;
import com.example.home.cleaning.bookings.entity.BookingCleaner;
import com.example.home.cleaning.bookings.repository.BookingCleanerRepository;
import com.example.home.cleaning.bookings.repository.BookingRepository;
import com.example.home.cleaning.bookings.repository.VehicleCleanerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private VehicleCleanerRepository vehicleCleanerRepository;
    @Mock
    private BookingCleanerRepository bookingCleanerRepository;

    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(bookingRepository, vehicleCleanerRepository, bookingCleanerRepository);
    }

    @Nested
    class CreateBooking {
        @Test
        @DisplayName("Create Booking")
        void createBooking() {
            var expectedBookingId = UUID.randomUUID();
            var startDateTime = LocalDateTime.now();
            var endDateTime = LocalDateTime.now().plusHours(2);

            var timeSlot = new TimeSlot(startDateTime, endDateTime);
            var customer = UUID.randomUUID();
            var bookingRequest = new BookingCreateRequest(customer, timeSlot, List.of(UUID.randomUUID()));

            given(vehicleCleanerRepository
                    .findByCleanerIds(bookingRequest.selectedCleanerIds()))
                    .willReturn(Set.of());

            given(bookingRepository.checkBookingForCleaners(
                    bookingRequest.selectedCleanerIds(),
                    bookingRequest.requestedSchedule().startTime(),
                    bookingRequest.requestedSchedule().endTime()))
                    .willReturn(List.of());

            given(bookingRepository.save(any()))
                    .willReturn(new Booking(expectedBookingId,
                            bookingRequest.requestedSchedule().startTime(),
                            bookingRequest.requestedSchedule().endTime(),
                            UUID.randomUUID()));

            given(bookingCleanerRepository.saveAll(any()))
                    .willReturn(List.of());

            var bookingId = bookingService.createBooking(bookingRequest);


            assertEquals(bookingId, bookingId);
        }

        @Test
        @DisplayName("Booking Not created due to selected cleaners do not have the same vehicle")
        void createBookingWithDifferentVehicle() {
            var startDateTime = LocalDateTime.now();
            var endDateTime = LocalDateTime.now().plusHours(2);

            var timeSlot = new TimeSlot(startDateTime, endDateTime);
            var customer = UUID.randomUUID();
            var bookingRequest = new BookingCreateRequest(customer, timeSlot, List.of(UUID.randomUUID()));

            given(vehicleCleanerRepository
                    .findByCleanerIds(bookingRequest.selectedCleanerIds()))
                    .willReturn(Set.of(UUID.randomUUID(), UUID.randomUUID()));

            try {
                bookingService.createBooking(bookingRequest);
            } catch (IllegalArgumentException e) {
                assertEquals("Selected cleaners do not have the same vehicle", e.getMessage());
            }
        }

        @Test
        @DisplayName("Booking Not created due to selected cleaners are not available for the requested schedule")
        void createBookingWithNotAvailableCleaners() {
            var startDateTime = LocalDateTime.now();
            var endDateTime = LocalDateTime.now().plusHours(2);

            var timeSlot = new TimeSlot(startDateTime, endDateTime);
            var customer = UUID.randomUUID();
            var bookingRequest = new BookingCreateRequest(customer, timeSlot, List.of(UUID.randomUUID()));

            given(vehicleCleanerRepository
                    .findByCleanerIds(bookingRequest.selectedCleanerIds()))
                    .willReturn(Set.of());

            given(bookingRepository.checkBookingForCleaners(
                    bookingRequest.selectedCleanerIds(),
                    bookingRequest.requestedSchedule().startTime(),
                    bookingRequest.requestedSchedule().endTime()))
                    .willReturn(List.of(new Booking(UUID.randomUUID(), startDateTime, endDateTime, customer)));

            try {
                bookingService.createBooking(bookingRequest);
            } catch (IllegalArgumentException e) {
                assertEquals("Selected cleaners are not available for the requested schedule", e.getMessage());
            }
        }
    }

    @Nested
    class UpdateBooking {
        @Test
        @DisplayName("Update Booking")
        void updateBooking() {
            var bookingId = UUID.randomUUID();
            var cleanerId = UUID.randomUUID();
            var startDateTime = LocalDateTime.now();
            var endDateTime = LocalDateTime.now().plusHours(2);

            var timeSlot = new TimeSlot(startDateTime, endDateTime);
            var bookingUpdateRequest = new BookingUpdateRequest(timeSlot);

            given(bookingCleanerRepository.findByBookingId(bookingId))
                    .willReturn(List.of(new BookingCleaner(UUID.randomUUID(),  cleanerId, bookingId)));

            given(bookingRepository.checkBookingForCleaners(
                    List.of(cleanerId),
                    bookingUpdateRequest.requestedSchedule().startTime(),
                    bookingUpdateRequest.requestedSchedule().endTime()))
                    .willReturn(List.of());

            bookingService.updateBooking(bookingId, bookingUpdateRequest);

            verify(bookingRepository).updateAppointment(bookingId, startDateTime, endDateTime);
        }

        @Test
        @DisplayName("Booking Not updated due to selected cleaners are not available for the requested schedule")
        void updateBookingWithNotAvailableCleaners() {
            var bookingId = UUID.randomUUID();
            var startDateTime = LocalDateTime.now();
            var endDateTime = LocalDateTime.now().plusHours(2);

            var cleanerId = UUID.randomUUID();


            var timeSlot = new TimeSlot(startDateTime, endDateTime);
            var bookingUpdateRequest = new BookingUpdateRequest(timeSlot);

            given(bookingCleanerRepository.findByBookingId(bookingId))
                    .willReturn(List.of(new BookingCleaner(UUID.randomUUID(), cleanerId, bookingId)));

            given(bookingRepository.checkBookingForCleaners(
                    List.of(cleanerId),
                    bookingUpdateRequest.requestedSchedule().startTime(),
                    bookingUpdateRequest.requestedSchedule().endTime()))
                    .willReturn(List.of(new Booking(UUID.randomUUID(), startDateTime, endDateTime, UUID.randomUUID())));

            try {
                bookingService.updateBooking(bookingId, bookingUpdateRequest);
            } catch (IllegalArgumentException e) {
                assertEquals("Selected cleaners are not available for the requested schedule", e.getMessage());
            }
        }
    }


}