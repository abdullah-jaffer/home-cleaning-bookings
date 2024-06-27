package com.example.home.cleaning.bookings.service;

import com.example.home.cleaning.bookings.repository.CleanerRepository;
import com.example.home.cleaning.bookings.repository.projection.CleanerBookingProjection;
import com.example.home.cleaning.bookings.repository.projection.CleanerProjection;
import com.example.home.cleaning.bookings.dto.AvailabilityResponse;
import com.example.home.cleaning.bookings.utils.TimeConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AvailabilityServiceTest {


    @Mock
    private CleanerRepository cleanerRepository;

    private AvailabilityService availabilityService;

    @BeforeEach
    void setUp() {
        availabilityService = new AvailabilityService(cleanerRepository);
    }


    @Test
    @DisplayName("Return available times of everyone when just date filter is given")
    void getAvailabilitySuccess() {
        // given
        LocalDate currentDate = LocalDate.of(2024, 1,1);
        CleanerProjection cleaner1 = new CleanerProjection() {
            private final UUID cleanerId = UUID.randomUUID();
            private final String cleanerName = "John Doe 1";

            @Override
            public UUID getCleanerId() {
                return cleanerId;
            }

            @Override
            public String getCleanerName() {
                return cleanerName;
            }
        };

        CleanerProjection cleaner2 = new CleanerProjection() {
            private final UUID cleanerId = UUID.randomUUID();
            private final String cleanerName = "John Doe 2";

            @Override
            public UUID getCleanerId() {
                return cleanerId;
            }

            @Override
            public String getCleanerName() {
                return cleanerName;
            }
        };


        CleanerBookingProjection cleanerBooking1 = new CleanerBookingProjection() {
            private final LocalDateTime startTime = LocalDateTime.of(2024, 1,1,10,0);
            private final LocalDateTime endTime = LocalDateTime.of(2024, 1,1,12,0);

            @Override
            public UUID getCleanerId() {
                return cleaner1.getCleanerId();
            }

            @Override
            public String getCleanerName() {
                return cleaner1.getCleanerName();
            }

            @Override
            public LocalDateTime getStartTime() {
                return startTime;
            }

            @Override
            public LocalDateTime getEndTime() {
                return endTime;
            }
        };

        given(cleanerRepository.availableCleanersOnGivenDate(currentDate))
                .willReturn(List.of(cleaner1,cleaner2));

        given(cleanerRepository.findAllCleanersByIds(List.of(cleaner1.getCleanerId(), cleaner2.getCleanerId()), currentDate))
                .willReturn(List.of(cleanerBooking1));

        // when
        List<AvailabilityResponse> availabilities = availabilityService.getAvailabilities(currentDate, null, null);

        // cleaner 1
        assertEquals(cleaner1.getCleanerId(), availabilities.getFirst().cleanerId());
        assertEquals(cleaner1.getCleanerName(), availabilities.getFirst().name());
        assertEquals(2, availabilities.getFirst().availableTimes().size());
        assertEquals(LocalDateTime.of(2024, 1, 1, TimeConstants.START_HOUR, TimeConstants.START_MINUTE), availabilities.getFirst().availableTimes().getFirst().startTime());
        assertEquals(LocalDateTime.of(2024, 1, 1, 9, 30), availabilities.getFirst().availableTimes().getFirst().endTime());
        assertEquals(LocalDateTime.of(2024, 1, 1, 12, 30), availabilities.getFirst().availableTimes().getLast().startTime());
        assertEquals(LocalDateTime.of(2024, 1, 1, TimeConstants.END_HOUR, TimeConstants.END_MINUTE), availabilities.getFirst().availableTimes().getLast().endTime());

        // cleaner 2
        assertEquals(cleaner2.getCleanerId(), availabilities.getLast().cleanerId());
        assertEquals(cleaner2.getCleanerName(), availabilities.getLast().name());
        assertEquals(1, availabilities.getLast().availableTimes().size());
        assertEquals(LocalDateTime.of(2024, 1, 1, TimeConstants.START_HOUR, TimeConstants.START_MINUTE), availabilities.getLast().availableTimes().getFirst().startTime());
        assertEquals(LocalDateTime.of(2024, 1, 1, TimeConstants.END_HOUR, TimeConstants.END_MINUTE), availabilities.getLast().availableTimes().getFirst().endTime());




    }

    @Test
    @DisplayName("Return available times of everyone when date filter, time and is given")
    void getAvailabilitySuccessWithDuration() {

        LocalDate currentDate = LocalDate.of(2024, 1,1);
        final LocalTime startTime = LocalTime.of(11, 0);
        final LocalTime endTime = LocalTime.of(13, 0);

        LocalDateTime start = LocalDateTime.of(currentDate, startTime).minusMinutes(TimeConstants.BREAK_IN_MINUTES);
        LocalDateTime end = LocalDateTime.of(currentDate, endTime).plusMinutes(TimeConstants.BREAK_IN_MINUTES);

        CleanerProjection cleaner1 = new CleanerProjection() {
            private final UUID cleanerId = UUID.randomUUID();
            private final String cleanerName = "John Doe 1";

            @Override
            public UUID getCleanerId() {
                return cleanerId;
            }

            @Override
            public String getCleanerName() {
                return cleanerName;
            }
        };

        CleanerProjection cleaner2 = new CleanerProjection() {
            private final UUID cleanerId = UUID.randomUUID();
            private final String cleanerName = "John Doe 2";

            @Override
            public UUID getCleanerId() {
                return cleanerId;
            }

            @Override
            public String getCleanerName() {
                return cleanerName;
            }
        };


        CleanerBookingProjection cleanerBooking1 = new CleanerBookingProjection() {
            private final LocalDateTime startTime = LocalDateTime.of(2024, 1,1,10,0);
            private final LocalDateTime endTime = LocalDateTime.of(2024, 1,1,12,0);

            @Override
            public UUID getCleanerId() {
                return cleaner1.getCleanerId();
            }

            @Override
            public String getCleanerName() {
                return cleaner1.getCleanerName();
            }

            @Override
            public LocalDateTime getStartTime() {
                return startTime;
            }

            @Override
            public LocalDateTime getEndTime() {
                return endTime;
            }
        };

        given(cleanerRepository.availableCleanersOnGivenTime(start, end))
                .willReturn(List.of(cleaner1,cleaner2));

        given(cleanerRepository.findAllCleanersByIds(List.of(cleaner1.getCleanerId(), cleaner2.getCleanerId()), currentDate))
                .willReturn(List.of(cleanerBooking1));

        // when
        List<AvailabilityResponse> availabilities = availabilityService.getAvailabilities(currentDate, startTime, 2);

        // cleaner 1
        assertEquals(cleaner1.getCleanerId(), availabilities.getFirst().cleanerId());
        assertEquals(cleaner1.getCleanerName(), availabilities.getFirst().name());
        assertEquals(2, availabilities.getFirst().availableTimes().size());
        assertEquals(LocalDateTime.of(2024, 1, 1, TimeConstants.START_HOUR, TimeConstants.START_MINUTE), availabilities.getFirst().availableTimes().getFirst().startTime());
        assertEquals(LocalDateTime.of(2024, 1, 1, 9, 30), availabilities.getFirst().availableTimes().getFirst().endTime());
        assertEquals(LocalDateTime.of(2024, 1, 1, 12, 30), availabilities.getFirst().availableTimes().getLast().startTime());
        assertEquals(LocalDateTime.of(2024, 1, 1, TimeConstants.END_HOUR, TimeConstants.END_MINUTE), availabilities.getFirst().availableTimes().getLast().endTime());

        // cleaner 2
        assertEquals(cleaner2.getCleanerId(), availabilities.getLast().cleanerId());
        assertEquals(cleaner2.getCleanerName(), availabilities.getLast().name());
        assertEquals(1, availabilities.getLast().availableTimes().size());
        assertEquals(LocalDateTime.of(2024, 1, 1, TimeConstants.START_HOUR, TimeConstants.START_MINUTE), availabilities.getLast().availableTimes().getFirst().startTime());
        assertEquals(LocalDateTime.of(2024, 1, 1, TimeConstants.END_HOUR, TimeConstants.END_MINUTE), availabilities.getLast().availableTimes().getFirst().endTime());




    }

    @Test
    @DisplayName("Return empty List on a Friday")
    void getEmptyAvailabilityForFriday() {

        List<AvailabilityResponse> availabilities = availabilityService.getAvailabilities(LocalDate.of(2024, 6, 28), null, null);
        assertEquals(0, availabilities.size());
    }

    @Test
    @DisplayName("Throw IllegalArgumentException when hours are not provided together but time is")
    void throwExceptionWhenHoursNotProvided() {
        assertThrows(IllegalArgumentException.class, () -> availabilityService.getAvailabilities(LocalDate.of(2023, 1, 28), LocalTime.of(12,32), null));
    }

    @Test
    @DisplayName("Throw IllegalArgumentException when time provided is before cleaner worker schedule")
    void throwExceptionWhenTimeBeforeStart() {
        assertThrows(IllegalArgumentException.class, () -> availabilityService.getAvailabilities(LocalDate.of(2023, 1, 28), LocalTime.of(7,32), 6));
    }
}