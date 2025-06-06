package com.lrasata.tripDesignApp.rest.controller;

import com.lrasata.tripDesignApp.repository.TripRepository;
import com.lrasata.tripDesignApp.service.TripService;
import com.lrasata.tripDesignApp.service.dto.TripDTO;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/trips")
public class TripController {
  private static final Logger LOG = LoggerFactory.getLogger(TripController.class);

  @Autowired private TripService tripService;

  @Autowired private TripRepository tripRepository;

  @GetMapping
  public ResponseEntity<List<TripDTO>> getAllTrips(
      @RequestParam(required = false) String dateFilter) {
    LOG.debug("REST request to get all Trips with filter: {}", dateFilter);
    List<TripDTO> trips;

    if ("past".equalsIgnoreCase(dateFilter)) {
      trips = tripService.findTripsInPast();
    } else if ("future".equalsIgnoreCase(dateFilter)) {
      trips = tripService.findTripsInFuture();
    } else {
      trips = tripService.findAll();
    }

    return ResponseEntity.ok().body(trips);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TripDTO> getTripById(@PathVariable Long id) {
    LOG.debug("REST request to get Trip : {}", id);
    Optional<TripDTO> responseDTO = tripService.findOneById(id);
    return responseDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<TripDTO> createTrip(@Valid @RequestBody TripDTO tripDTO) {
    LOG.debug("REST request to create Trip : {}", tripDTO);
    // LOG.debug("REST request to save MobilePlan : {}", mobilePlanDTO);
    if (tripDTO.getId() != null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "A new trip cannot already have an ID");
    }
    TripDTO responseDTO = tripService.createTrip(tripDTO);
    return ResponseEntity.created(URI.create("/api/trips/" + responseDTO.getId()))
        .body(responseDTO);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TripDTO> updateTrip(
      @PathVariable Long id, @Valid @RequestBody TripDTO tripDTO) {
    LOG.debug("REST request to update Trip : {}, {}", id, tripDTO);

    if (tripDTO.getId() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A trip should have an ID");
    }
    if (!Objects.equals(id, tripDTO.getId())) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "URI endpoint and  request body don't match");
    }

    if (!tripRepository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found");
    }

    TripDTO updatedTrip = tripService.updateTrip(id, tripDTO);
    return ResponseEntity.ok(updatedTrip);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
    LOG.debug("REST request to delete Trip : {}", id);
    tripService.deleteTrip(id);
    return ResponseEntity.noContent().build();
  }
}
