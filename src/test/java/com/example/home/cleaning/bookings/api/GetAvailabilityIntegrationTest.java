package com.example.home.cleaning.bookings.api;

import com.example.home.cleaning.bookings.Application;
import com.example.home.cleaning.bookings.repository.BookingCleanerRepository;
import com.example.home.cleaning.bookings.repository.BookingRepository;
import com.example.home.cleaning.bookings.dto.response.AvailabilityResponse;
import com.example.home.cleaning.bookings.entity.Booking;
import com.example.home.cleaning.bookings.entity.BookingCleaner;
import com.example.home.cleaning.bookings.runner.PostgresRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@SpringBootTest(
        classes = {Application.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ExtendWith(PostgresRunner.class)
class GetAvailabilityIntegrationTest {

    private final BookingRepository bookingRepository;

    private final BookingCleanerRepository bookingCleanerRepository;

    private final TestRestTemplate testRestTemplate;


    public GetAvailabilityIntegrationTest(
            @Autowired BookingRepository bookingRepository,
            @Autowired BookingCleanerRepository bookingCleanerRepository,
            @Autowired TestRestTemplate testRestTemplate) {
        this.bookingRepository = bookingRepository;
        this.bookingCleanerRepository = bookingCleanerRepository;
        this.testRestTemplate = testRestTemplate;
    }


    @BeforeEach
    public  void setUp() {
        bookingRepository.save(new Booking(
                UUID.fromString("bf1975ff-b731-4569-8d6a-9099d5346169"),
                LocalDateTime.of(2024, 1,1,10,0),
                LocalDateTime.of(2024, 1,1,12,0),
                UUID.randomUUID()
        ));

        UUID cleanerId = UUID.fromString("2b96890a-213e-4618-bf1d-49ab63c91c88");
        bookingCleanerRepository.save(
                new BookingCleaner(
                        UUID.fromString("bf1975ff-b731-4569-8d6a-9099d5346169"),
                        cleanerId,
                        UUID.fromString("bf1975ff-b731-4569-8d6a-9099d5346169")
                )
        );
    }


    @Test
    void testGetAvailability() {
        AvailabilityResponse[] availability = testRestTemplate.getForObject("/v1/availabilities?date=2024-05-06", AvailabilityResponse[].class);
        Assertions.assertEquals(25, availability.length);

        var cleaner1 = Arrays.stream(availability).filter(item -> item.cleanerId().equals(UUID.fromString("2b96890a-213e-4618-bf1d-49ab63c91c88"))).findFirst().get();

        Assertions.assertEquals(UUID.fromString("2b96890a-213e-4618-bf1d-49ab63c91c88"), cleaner1.cleanerId());
        Assertions.assertEquals("Cleaner 1", cleaner1.name());
        Assertions.assertEquals(1, cleaner1.availableTimes().size());

    }
}
