package com.example.home.cleaning.bookings.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    private UUID id;

    private String number;

    public UUID getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Vehicle(UUID id, String number) {
        this.id = id;
        this.number = number;
    }

    public Vehicle() {
    }
}
