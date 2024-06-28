package com.example.home.cleaning.bookings.api;

import com.example.home.cleaning.bookings.Application;
import com.example.home.cleaning.bookings.dto.TimeSlot;
import com.example.home.cleaning.bookings.dto.request.BookingCreateRequest;
import com.example.home.cleaning.bookings.repository.BookingRepository;
import com.example.home.cleaning.bookings.runner.PostgresRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.fail;

@SpringBootTest(
        classes = {Application.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ExtendWith(PostgresRunner.class)
class PostBookingCreationIntegrationTest {


    private final TestRestTemplate testRestTemplate;
    private final BookingRepository bookingRepository;


    public PostBookingCreationIntegrationTest(
            @Autowired BookingRepository bookingRepository,
            @Autowired TestRestTemplate testRestTemplate) {
        this.testRestTemplate = testRestTemplate;
        this.bookingRepository = bookingRepository;
    }

    @Nested
    class CreateBooking {
        @Test
        void testCreateBooking() {
            UUID id = testRestTemplate.postForObject("/v1/booking", new BookingCreateRequest(
                    UUID.randomUUID(),
                    new TimeSlot(
                            LocalDateTime.of(2024, 1, 1, 10, 0),
                            LocalDateTime.of(2024, 1, 1, 12, 0)
                    ),
                    List.of(UUID.fromString("dc449ca0-fa4c-412c-bb0d-7c4d7caa0dd0"))
            ), UUID.class);


            Assertions.assertNotNull(id);
            var booking = bookingRepository.findById(id).get();

            Assertions.assertEquals(id, booking.getId());
            Assertions.assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), booking.getStartTime());
            Assertions.assertEquals(LocalDateTime.of(2024, 1, 1, 12, 0), booking.getEndTime());
            ;

        }

    }

}

