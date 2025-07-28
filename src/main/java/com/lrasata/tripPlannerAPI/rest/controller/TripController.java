package com.lrasata.tripPlannerAPI.rest.controller;

import com.lrasata.tripPlannerAPI.repository.TripRepository;
import com.lrasata.tripPlannerAPI.service.TripService;
import com.lrasata.tripPlannerAPI.service.dto.PaginatedResponse;
import com.lrasata.tripPlannerAPI.service.dto.TripDTO;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/trips")
public class TripController {
  private static final Logger LOG = LoggerFactory.getLogger(TripController.class);

  private final TripService tripService;

  private final TripRepository tripRepository;

  public TripController(TripService tripService, TripRepository tripRepository) {
    this.tripService = tripService;
    this.tripRepository = tripRepository;
  }

  @GetMapping
  public ResponseEntity<PaginatedResponse<TripDTO>> getAllTrips(
      @RequestParam(required = false) String dateFilter,
      @RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    LOG.debug(
        "REST request to get all Trips with dateFilter={}, keyword={}, page={}, size={}",
        dateFilter,
        keyword,
        page,
        size);

    Pageable pageable = PageRequest.of(page, size, Sort.by("startDate").descending());
    Page<TripDTO> trips;

    if (keyword != null && !keyword.isBlank()) {
      trips = tripService.findTripsByKeyword(keyword, pageable);
    } else {
      switch (dateFilter != null ? dateFilter.toLowerCase() : "") {
        case "past" -> trips = tripService.findTripsInPast(pageable);
        case "future" -> trips = tripService.findTripsInFuture(pageable);
        default -> trips = tripService.findAll(pageable);
      }
    }

    return ResponseEntity.ok(new PaginatedResponse<>(trips));
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

    if (tripDTO.getId() != null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "A new trip cannot already have an ID");
    }

    if (tripDTO.getReturnDate().isBefore(tripDTO.getDepartureDate())) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "The return date should be AFTER the departure date");
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

    if (tripDTO.getReturnDate().isBefore(tripDTO.getDepartureDate())) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "The return date should be AFTER the departure date");
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
