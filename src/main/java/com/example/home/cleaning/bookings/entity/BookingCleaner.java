package com.example.home.cleaning.bookings.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "booking_cleaners")
public class BookingCleaner {
    @Id
    private UUID id;

    private UUID cleanerId;

    private UUID bookingId;


    public BookingCleaner() {
    }

    public BookingCleaner(UUID id, UUID cleanerId, UUID bookingId) {
        this.bookingId = bookingId;
        this.id = id;
        this.cleanerId = cleanerId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCleanerId() {
        return cleanerId;
    }

    public void setCleanerId(UUID cleanerId) {
        this.cleanerId = cleanerId;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }
}
