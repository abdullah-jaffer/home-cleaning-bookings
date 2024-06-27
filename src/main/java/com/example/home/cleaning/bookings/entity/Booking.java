package com.example.home.cleaning.bookings.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    private UUID id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private UUID customerId;

    public UUID getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }




    public Booking(UUID id, LocalDateTime startTime, LocalDateTime endTime, UUID customerId) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerId = customerId;
    }

    public Booking() {
    }


}
