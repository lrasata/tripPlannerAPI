package com.lrasata.tripPlannerAPI.rest.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.service.TripService;
import com.lrasata.tripPlannerAPI.service.dto.PaginatedResponse;
import com.lrasata.tripPlannerAPI.service.dto.TripDTO;
import java.time.LocalDate;
import java.util.ArrayList;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.server.ResponseStatusException;

class TripControllerTest {

  @InjectMocks private TripController tripController;

  @Mock private TripService tripService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private TripDTO createTrip(Long id, String name) {
    return createTrip(id, name, new ArrayList<>()); // default participantId = 5
  }

  private TripDTO createTrip(Long id, String name, List<Long> participantIds) {
    TripDTO trip = new TripDTO();
    trip.setId(id);
    trip.setName(name);
    trip.setDescription("Description");
    trip.setDepartureDate(LocalDate.now().minusDays(1));
    trip.setReturnDate(LocalDate.now().plusDays(3));
    trip.setParticipantCount(5);
    trip.setParticipantIds(participantIds);
    return trip;
  }

  @Test
  void getAllTrips_noFilter_returnsAllPaginatedResult_AsAdmin() {
    List<TripDTO> trips = List.of(createTrip(1L, "Trip A"), createTrip(2L, "Trip B"));
    Page<TripDTO> pagedResult = new PageImpl<>(trips);
    when(tripService.findAll(any(Pageable.class))).thenReturn(pagedResult);

    // Mock Authentication
    Authentication authentication = mock(Authentication.class);

    // Mock User
    User mockUser = mock(User.class);
    when(authentication.getPrincipal()).thenReturn(mockUser);
    when(mockUser.getId()).thenReturn(42L);
    when(mockUser.getAuthorities())
        .thenAnswer(invocation -> List.of(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN")));

    ResponseEntity<PaginatedResponse<TripDTO>> response =
        tripController.getAllTrips(null, null, 0, 10, authentication);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, response.getBody().getTotalElements());
    assertEquals(1, response.getBody().getTotalPages());
  }

  @Test
  void getAllTrips_noFilter_returnsAllPaginatedResult_AsParticipant() {
    // Mock User
    User mockUser = mock(User.class);
    mockUser.setId(2L);
    when(mockUser.getId()).thenReturn(2L);

    // Mock Authentication
    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(mockUser);
    when(mockUser.getAuthorities())
        .thenAnswer(invocation -> List.of(new SimpleGrantedAuthority("ROLE_PARTICIPANT")));

    List<Long> participantIds = new ArrayList<>();
    participantIds.add(2L);
    List<TripDTO> trips = List.of(createTrip(1L, "Trip A", participantIds));
    Page<TripDTO> pagedResult = new PageImpl<>(trips);
    when(tripService.findTripsByParticipant(eq(2L), any(Pageable.class))).thenReturn(pagedResult);

    ResponseEntity<PaginatedResponse<TripDTO>> response =
        tripController.getAllTrips(null, null, 0, 10, authentication);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().getTotalElements());
    assertEquals(1, response.getBody().getTotalPages());
  }

  @Test
  void getAllTrips_withPastFilter_returnsPastTrips_AsAdmin() {
    List<TripDTO> trips = List.of(createTrip(3L, "Old Trip"));
    Page<TripDTO> pagedResult = new PageImpl<>(trips);
    when(tripService.findTripsInPast(any(Pageable.class))).thenReturn(pagedResult);

    // Mock Authentication
    Authentication authentication = mock(Authentication.class);

    // Mock User
    User mockUser = mock(User.class);
    when(authentication.getPrincipal()).thenReturn(mockUser);
    when(mockUser.getId()).thenReturn(42L);
    when(mockUser.getAuthorities())
        .thenAnswer(invocation -> List.of(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN")));

    ResponseEntity<PaginatedResponse<TripDTO>> response =
        tripController.getAllTrips("past", null, 0, 10, authentication);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Old Trip", response.getBody().getContent().get(0).getName());
  }

  @Test
  void getAllTrips_withPastFilter_returnsPastTrips_AsParticipant() {
    // Mock User
    User mockUser = mock(User.class);
    mockUser.setId(2L);
    when(mockUser.getId()).thenReturn(2L);

    // Mock Authentication
    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(mockUser);
    when(mockUser.getAuthorities())
        .thenAnswer(invocation -> List.of(new SimpleGrantedAuthority("ROLE_PARTICIPANT")));

    List<Long> participantIds = new ArrayList<>();
    participantIds.add(2L);
    List<TripDTO> trips = List.of(createTrip(1L, "Old Trip", participantIds));
    Page<TripDTO> pagedResult = new PageImpl<>(trips);
    when(tripService.findTripsByParticipantInPast(eq(2L), any(Pageable.class)))
        .thenReturn(pagedResult);

    ResponseEntity<PaginatedResponse<TripDTO>> response =
        tripController.getAllTrips("past", null, 0, 10, authentication);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Old Trip", response.getBody().getContent().get(0).getName());
  }

  @Test
  void getAllTrips_withFutureFilter_returnsFutureTrips_AsAdmin() {
    List<TripDTO> trips = List.of(createTrip(4L, "Future Trip"));
    Page<TripDTO> pagedResult = new PageImpl<>(trips);
    when(tripService.findTripsInFuture(any(Pageable.class))).thenReturn(pagedResult);

    // Mock Authentication
    Authentication authentication = mock(Authentication.class);

    // Mock User
    User mockUser = mock(User.class);
    when(authentication.getPrincipal()).thenReturn(mockUser);
    when(mockUser.getId()).thenReturn(42L);
    when(mockUser.getAuthorities())
        .thenAnswer(invocation -> List.of(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN")));

    ResponseEntity<PaginatedResponse<TripDTO>> response =
        tripController.getAllTrips("future", null, 0, 10, authentication);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Future Trip", response.getBody().getContent().get(0).getName());
  }

  @Test
  void getAllTrips_withFutureFilter_returnsFutureTrips_AsParticipant() {

    // Mock User
    User mockUser = mock(User.class);
    mockUser.setId(2L);
    when(mockUser.getId()).thenReturn(2L);

    // Mock Authentication
    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(mockUser);
    when(mockUser.getAuthorities())
        .thenAnswer(invocation -> List.of(new SimpleGrantedAuthority("ROLE_PARTICIPANT")));

    List<Long> participantIds = new ArrayList<>();
    participantIds.add(2L);
    List<TripDTO> trips = List.of(createTrip(1L, "Future Trip", participantIds));
    Page<TripDTO> pagedResult = new PageImpl<>(trips);
    when(tripService.findTripsByParticipantInFuture(eq(2L), any(Pageable.class)))
        .thenReturn(pagedResult);

    ResponseEntity<PaginatedResponse<TripDTO>> response =
        tripController.getAllTrips("future", null, 0, 10, authentication);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Future Trip", response.getBody().getContent().get(0).getName());
  }

  @Test
  void getAllTrips_withKeywordFilter_returnsFilteredTrips_AsAdmin() {
    List<TripDTO> trips = List.of(createTrip(5L, "Trip A to return"));
    Page<TripDTO> pagedResult = new PageImpl<>(trips);
    when(tripService.findTripsByKeyword(eq("trip a"), any(Pageable.class))).thenReturn(pagedResult);

    // Mock Authentication
    Authentication authentication = mock(Authentication.class);

    // Mock User
    User mockUser = mock(User.class);
    when(authentication.getPrincipal()).thenReturn(mockUser);
    when(mockUser.getId()).thenReturn(42L);
    when(mockUser.getAuthorities())
        .thenAnswer(invocation -> List.of(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN")));

    ResponseEntity<PaginatedResponse<TripDTO>> response =
        tripController.getAllTrips(null, "trip a", 0, 10, authentication);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Trip A to return", response.getBody().getContent().get(0).getName());
  }

  @Test
  void getAllTrips_withKeywordFilterAndFutureFilter_returnsFilteredTrips_AsAdmin() {
    List<TripDTO> trips = List.of(createTrip(6L, "Future Trip"));
    Page<TripDTO> pagedResult = new PageImpl<>(trips);
    when(tripService.findTripsByKeyword(eq("future trip"), any(Pageable.class)))
        .thenReturn(pagedResult);

    // Mock Authentication
    Authentication authentication = mock(Authentication.class);

    // Mock User
    User mockUser = mock(User.class);
    when(authentication.getPrincipal()).thenReturn(mockUser);
    when(mockUser.getId()).thenReturn(42L);
    when(mockUser.getAuthorities())
        .thenAnswer(invocation -> List.of(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN")));

    ResponseEntity<PaginatedResponse<TripDTO>> response =
        tripController.getAllTrips("future", "future trip", 0, 10, authentication);

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
    when(tripService.findOneById(1L)).thenReturn(Optional.of(update));
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
