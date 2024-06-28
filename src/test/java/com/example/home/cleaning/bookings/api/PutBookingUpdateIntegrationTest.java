package com.example.home.cleaning.bookings.api;

import com.example.home.cleaning.bookings.Application;
import com.example.home.cleaning.bookings.dto.TimeSlot;
import com.example.home.cleaning.bookings.dto.request.BookingCreateRequest;
import com.example.home.cleaning.bookings.dto.request.BookingUpdateRequest;
import com.example.home.cleaning.bookings.entity.Booking;
import com.example.home.cleaning.bookings.entity.BookingCleaner;
import com.example.home.cleaning.bookings.repository.BookingCleanerRepository;
import com.example.home.cleaning.bookings.repository.BookingRepository;
import com.example.home.cleaning.bookings.runner.PostgresRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootTest(
        classes = {Application.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ExtendWith(PostgresRunner.class)
class PutBookingUpdateIntegrationTest {


    private final TestRestTemplate testRestTemplate;
    private final BookingRepository bookingRepository;
    private final BookingCleanerRepository bookingCleanerRepository;


    public PutBookingUpdateIntegrationTest(
            @Autowired BookingRepository bookingRepository,
            @Autowired BookingCleanerRepository bookingCleanerRepository,
            @Autowired TestRestTemplate testRestTemplate) {
        this.testRestTemplate = testRestTemplate;
        this.bookingRepository = bookingRepository;
        this.bookingCleanerRepository = bookingCleanerRepository;
    }

    @Nested
    class UpdateBooking {

        UUID bookingId = UUID.randomUUID();

        @BeforeEach
        void setUp() {
            var startDateTime = LocalDateTime.of(2022, 1, 1, 10, 0);
            var endDateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
            var booking = new Booking(bookingId, startDateTime, endDateTime, UUID.randomUUID());
            bookingRepository.save(booking);
            var bookingCleaner = new BookingCleaner(UUID.randomUUID(), UUID.fromString("cead283e-bef9-47d7-bf61-0123aaa37a54"), bookingId);
            bookingCleanerRepository.save(bookingCleaner);
        }

        @Test
        void testUpdateBooking() {
            LocalDateTime endTime = LocalDateTime.of(2024, 1, 1, 14, 0);
            LocalDateTime startTime = LocalDateTime.of(2024, 1, 1, 10, 0);
            testRestTemplate.put("/v1/booking/" + bookingId, new BookingUpdateRequest(
                    new TimeSlot(
                            startTime,
                            endTime
                    )));


            var booking = bookingRepository.findById(bookingId).get();

            Assertions.assertEquals(startTime, booking.getStartTime());
            Assertions.assertEquals(endTime, booking.getEndTime());

        }

    }

}

