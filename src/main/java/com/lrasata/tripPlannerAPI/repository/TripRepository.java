package com.lrasata.tripPlannerAPI.repository;

import com.lrasata.tripPlannerAPI.entity.Trip;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
  Page<Trip> findByDepartureDateBefore(LocalDate date, Pageable pageable);

  Page<Trip> findByDepartureDateAfter(LocalDate date, Pageable pageable);

  Page<Trip> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
      String name, String description, Pageable pageable);

  @Query("SELECT t FROM Trip t JOIN t.participants p WHERE p.id = :userId")
  Page<Trip> findByParticipants_Id(@Param("userId") Long userId, Pageable pageable);

  @Query(
      """
    SELECT t FROM Trip t
    JOIN t.participants p
    WHERE p.id = :userId
      AND (LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
  """)
  Page<Trip> findByParticipantAndKeyword(
      @Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);

  @Query(
      """
    SELECT t FROM Trip t
    JOIN t.participants p
    WHERE p.id = :userId
      AND t.departureDate < CURRENT_DATE
  """)
  Page<Trip> findByParticipantInPast(@Param("userId") Long userId, Pageable pageable);

  @Query(
      """
    SELECT t FROM Trip t
    JOIN t.participants p
    WHERE p.id = :userId
      AND t.departureDate >= CURRENT_DATE
  """)
  Page<Trip> findByParticipantInFuture(@Param("userId") Long userId, Pageable pageable);
}
