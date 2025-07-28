package com.lrasata.tripPlannerAPI.repository;

import com.lrasata.tripPlannerAPI.entity.Trip;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
  Page<Trip> findByDepartureDateBefore(LocalDate date, Pageable pageable);

  Page<Trip> findByDepartureDateAfter(LocalDate date, Pageable pageable);

  Page<Trip> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
      String name, String description, Pageable pageable);
}
