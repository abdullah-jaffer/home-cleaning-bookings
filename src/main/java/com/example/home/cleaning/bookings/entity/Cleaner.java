package com.example.home.cleaning.bookings.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "cleaners")
public class Cleaner {
    @Id
    private UUID id;

    private String name;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Cleaner(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public Cleaner() {
    }
}
