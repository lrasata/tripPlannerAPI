package com.lrasata.tripPlannerAPI.rest.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.lrasata.tripPlannerAPI.repository.TripRepository;
import com.lrasata.tripPlannerAPI.service.TripService;
import com.lrasata.tripPlannerAPI.service.dto.PaginatedResponse;
import com.lrasata.tripPlannerAPI.service.dto.TripDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

class TripControllerTest {

  @InjectMocks private TripController tripController;

  @Mock private TripService tripService;

  @Mock private TripRepository tripRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private TripDTO createTrip(Long id, String name) {
    TripDTO trip = new TripDTO();
    trip.setId(id);
    trip.setName(name);
    trip.setDescription("Description");
    trip.setDepartureDate(LocalDate.now().minusDays(1));
    trip.setReturnDate(LocalDate.now().plusDays(3));
    trip.setParticipantCount(5);
    return trip;
  }

  @Test
  void getAllTrips_noFilter_returnsAllPaginatedResult() {
    List<TripDTO> trips = List.of(createTrip(1L, "Trip A"), createTrip(2L, "Trip B"));
    Page<TripDTO> pagedResult = new PageImpl<>(trips);
    when(tripService.findAll(any(Pageable.class))).thenReturn(pagedResult);

    ResponseEntity<PaginatedResponse<TripDTO>> response =
        tripController.getAllTrips(null, null, 0, 10);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, response.getBody().getTotalElements());
    assertEquals(1, response.getBody().getTotalPages());
  }

  @Test
  void getAllTrips_withPastFilter_returnsPastTrips() {
    List<TripDTO> trips = List.of(createTrip(3L, "Old Trip"));
    Page<TripDTO> pagedResult = new PageImpl<>(trips);
    when(tripService.findTripsInPast(any(Pageable.class))).thenReturn(pagedResult);

    ResponseEntity<PaginatedResponse<TripDTO>> response =
        tripController.getAllTrips("past", null, 0, 10);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Old Trip", response.getBody().getContent().get(0).getName());
  }

  @Test
  void getAllTrips_withFutureFilter_returnsFutureTrips() {
    List<TripDTO> trips = List.of(createTrip(4L, "Future Trip"));
    Page<TripDTO> pagedResult = new PageImpl<>(trips);
    when(tripService.findTripsInFuture(any(Pageable.class))).thenReturn(pagedResult);

    ResponseEntity<PaginatedResponse<TripDTO>> response =
        tripController.getAllTrips("future", null, 0, 10);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Future Trip", response.getBody().getContent().get(0).getName());
  }

  @Test
  void getAllTrips_withKeywordFilter_returnsFilteredTrips() {
    List<TripDTO> trips = List.of(createTrip(5L, "Trip A to return"));
    Page<TripDTO> pagedResult = new PageImpl<>(trips);
    when(tripService.findTripsByKeyword(eq("trip a"), any(Pageable.class))).thenReturn(pagedResult);

    ResponseEntity<PaginatedResponse<TripDTO>> response =
        tripController.getAllTrips(null, "trip a", 0, 10);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Trip A to return", response.getBody().getContent().get(0).getName());
  }

  @Test
  void getAllTrips_withKeywordFilterAndFutureFilter_returnsFilteredTrips() {
    List<TripDTO> trips = List.of(createTrip(6L, "Future Trip"));
    Page<TripDTO> pagedResult = new PageImpl<>(trips);
    when(tripService.findTripsByKeyword(eq("future trip"), any(Pageable.class)))
        .thenReturn(pagedResult);

    ResponseEntity<PaginatedResponse<TripDTO>> response =
        tripController.getAllTrips("future", "future trip", 0, 10);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Future Trip", response.getBody().getContent().get(0).getName());
  }

  @Test
  void getTripById_found() {
    TripDTO trip = createTrip(1L, "Trip A");
    when(tripService.findOneById(1L)).thenReturn(Optional.of(trip));

    ResponseEntity<TripDTO> response = tripController.getTripById(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Trip A", response.getBody().getName());
  }

  @Test
  void getTripById_notFound() {
    when(tripService.findOneById(999L)).thenReturn(Optional.empty());

    ResponseEntity<TripDTO> response = tripController.getTripById(999L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void createTrip_success() {
    TripDTO request = createTrip(null, "New Trip");
    TripDTO saved = createTrip(10L, "New Trip");
    when(tripService.createTrip(request)).thenReturn(saved);

    ResponseEntity<TripDTO> response = tripController.createTrip(request);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(10L, response.getBody().getId());
  }

  @Test
  void createTrip_withId_throwsException() {
    TripDTO request = createTrip(5L, "Invalid Trip");

    ResponseStatusException ex =
        assertThrows(
            ResponseStatusException.class,
            () -> {
              tripController.createTrip(request);
            });

    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
  }

  @Test
  void createTrip_withInvalidDates_throwsException() {
    TripDTO request = createTrip(null, "Invalid Trip");
    request.setDepartureDate(LocalDate.now().plusDays(3));
    request.setReturnDate(LocalDate.now().minusDays(1));

    ResponseStatusException ex =
        assertThrows(
            ResponseStatusException.class,
            () -> {
              tripController.createTrip(request);
            });

    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
  }

  @Test
  void updateTrip_success() {
    TripDTO update = createTrip(1L, "Updated Trip");
    when(tripRepository.existsById(1L)).thenReturn(true);
    when(tripService.updateTrip(1L, update)).thenReturn(update);

    ResponseEntity<TripDTO> response = tripController.updateTrip(1L, update);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Updated Trip", response.getBody().getName());
  }

  @Test
  void updateTrip_idMismatch_throwsException() {
    TripDTO update = createTrip(2L, "Mismatch Trip");

    ResponseStatusException ex =
        assertThrows(
            ResponseStatusException.class,
            () -> {
              tripController.updateTrip(1L, update);
            });

    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
  }

  @Test
  void updateTrip_tripNotFound_throwsException() {
    TripDTO update = createTrip(5L, "Nonexistent Trip");
    when(tripRepository.existsById(5L)).thenReturn(false);

    ResponseStatusException ex =
        assertThrows(
            ResponseStatusException.class,
            () -> {
              tripController.updateTrip(5L, update);
            });

    assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
  }

  @Test
  void updateTrip_tripWithInvalidDates_throwsException() {
    TripDTO update = createTrip(5L, "Trip with invalid dates");
    update.setDepartureDate(LocalDate.now().plusDays(3));
    update.setReturnDate(LocalDate.now().minusDays(1));

    when(tripRepository.existsById(5L)).thenReturn(true);

    ResponseStatusException ex =
        assertThrows(
            ResponseStatusException.class,
            () -> {
              tripController.updateTrip(5L, update);
            });

    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
  }

  @Test
  void updateTrip_nullId_throwsException() {
    TripDTO update = createTrip(null, "No ID Trip");

    ResponseStatusException ex =
        assertThrows(
            ResponseStatusException.class,
            () -> {
              tripController.updateTrip(1L, update);
            });

    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
  }

  @Test
  void deleteTrip_success() {
    doNothing().when(tripService).deleteTrip(1L);

    ResponseEntity<Void> response = tripController.deleteTrip(1L);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(tripService, times(1)).deleteTrip(1L);
  }
}
