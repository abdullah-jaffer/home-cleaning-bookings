package com.example.home.cleaning.bookings.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "booking_cleaners")
public class BookingCleaner {
    @Id
    private UUID id;

    private UUID cleanerId;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    Booking booking;


    public BookingCleaner() {
    }

    public BookingCleaner(UUID id, UUID cleanerId, Booking booking) {
        this.booking = booking;
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
}
