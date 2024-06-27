package com.example.home.cleaning.bookings.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
}
