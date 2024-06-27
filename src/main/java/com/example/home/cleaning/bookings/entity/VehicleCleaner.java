package com.example.home.cleaning.bookings.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "vehicle_cleaners")
public class VehicleCleaner {
    @Id
    private UUID id;

    private UUID cleanerId;

    private UUID bookingId;

    public VehicleCleaner() {
    }

    public VehicleCleaner(UUID id, UUID cleanerId, UUID bookingId) {
        this.id = id;
        this.cleanerId = cleanerId;
        this.bookingId = bookingId;
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

    public UUID getBookingId() {
        return bookingId;
    }

    public void setCleanerId(UUID cleanerId) {
        this.cleanerId = cleanerId;
    }
}
