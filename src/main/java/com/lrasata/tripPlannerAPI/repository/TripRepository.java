package com.lrasata.tripPlannerAPI.repository;

import com.lrasata.tripPlannerAPI.entity.Trip;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
  List<Trip> findByDepartureDateBefore(LocalDate date);

  List<Trip> findByDepartureDateAfter(LocalDate date);

  List<Trip> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
      String name, String description);
}
