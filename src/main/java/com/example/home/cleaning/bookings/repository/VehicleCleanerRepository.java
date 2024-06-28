package com.example.home.cleaning.bookings.repository;

import com.example.home.cleaning.bookings.entity.VehicleCleaner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface VehicleCleanerRepository extends JpaRepository<VehicleCleaner, UUID> {

    /**
     * Find all vehicle ids by cleaner ids
     * @param cleanerIds
     * @return Set of vehicle ids
     */
    @Query("SELECT vehicleCleaner.vehicleId FROM VehicleCleaner vehicleCleaner WHERE vehicleCleaner.cleanerId IN :cleanerIds")
    Set<UUID> findByCleanerIds(List<UUID> cleanerIds);
}
