package com.lrasata.tripDesignApp.repository;

import com.lrasata.tripDesignApp.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
}