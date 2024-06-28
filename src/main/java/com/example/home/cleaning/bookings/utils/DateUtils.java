package com.example.home.cleaning.bookings.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateUtils {

    public static boolean isFriday(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.FRIDAY;
    }

    public static LocalDateTime addBreak(LocalDateTime dateTime) {
        return dateTime.minusMinutes(TimeConstants.BREAK_IN_MINUTES);
    }

    public static LocalDateTime removeBreak(LocalDateTime dateTime) {
        return dateTime.plusMinutes(TimeConstants.BREAK_IN_MINUTES);
    }

    public static boolean checkBoundaryDate(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return startDateTime.getHour() < TimeConstants.START_HOUR || endDateTime.getHour() > TimeConstants.END_HOUR;
    }

    public record SanitizedTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
    }

    public static SanitizedTime applyTimeSanitization(LocalDate date, LocalTime time, Integer hours) {
        var startDateTime = LocalDateTime.of(date, time);
        var endDateTime = startDateTime.plusHours(hours);

        startDateTime = addBreak(startDateTime);
        endDateTime = removeBreak(endDateTime);

        if (DateUtils.checkBoundaryDate(startDateTime, endDateTime)) {
            throw new IllegalArgumentException(String.format("Time should be between %s:00 and %s:00", TimeConstants.START_HOUR, TimeConstants.END_HOUR)
            );
        }
        return new SanitizedTime(startDateTime, endDateTime);
    }
}
