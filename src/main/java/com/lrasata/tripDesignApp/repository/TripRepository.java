package com.lrasata.tripDesignApp.repository;

import com.lrasata.tripDesignApp.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByDepartureDateBefore(LocalDate date);

    List<Trip> findByDepartureDateAfter(LocalDate date);

}