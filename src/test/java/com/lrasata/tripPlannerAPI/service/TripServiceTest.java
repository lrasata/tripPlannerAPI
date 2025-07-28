package com.lrasata.tripPlannerAPI.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.lrasata.tripPlannerAPI.entity.Location;
import com.lrasata.tripPlannerAPI.entity.Trip;
import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.repository.TripRepository;
import com.lrasata.tripPlannerAPI.repository.UserRepository;
import com.lrasata.tripPlannerAPI.service.dto.LocationDTO;
import com.lrasata.tripPlannerAPI.service.dto.TripDTO;
import com.lrasata.tripPlannerAPI.service.mapper.TripMapper;
import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class TripServiceTest {

  @Mock private TripRepository tripRepository;

  @Mock private LocationService locationService;

  @Mock private UserRepository userRepository;

  @Mock private TripMapper tripMapper;

  @InjectMocks private TripService tripService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private Trip createTrip(Long id) {
    Trip trip = new Trip();
    trip.setId(id);
    trip.setName("Trip A");
    trip.setDescription("Description");
    return trip;
  }

  private TripDTO createTripDTO(Long id) {
    TripDTO dto = new TripDTO();
    dto.setId(id);
    dto.setName("Summer Trip");
    dto.setDepartureDate(LocalDate.now().plusDays(10));
    dto.setReturnDate(LocalDate.now().plusDays(20));
    dto.setDepartureLocation(new LocationDTO());
    dto.setArrivalLocation(new LocationDTO());
    dto.setParticipantIds(List.of(1L, 2L));
    dto.setParticipantCount(1);
    return dto;
  }

  @Test
  void findAll_returnsMappedTrips() {
    Pageable pageable = PageRequest.of(0, 10);
    List<Trip> trips = List.of(createTrip(1L), createTrip(2L));
    Page<Trip> pagedResult = new PageImpl<>(trips);

    when(tripRepository.findAll(pageable)).thenReturn(pagedResult);
    when(tripMapper.toDto(any())).thenReturn(new TripDTO());

    Page<TripDTO> result = tripService.findAll(pageable);

    assertEquals(2, result.stream().toList().size());
  }

  @Test
  void findTripsInPast_returnsFilteredTrips() {
    List<Trip> trips = List.of(createTrip(1L));
    Page<Trip> pagedResult = new PageImpl<>(trips);

    when(tripRepository.findByDepartureDateBefore(any(), any(Pageable.class)))
        .thenReturn(pagedResult);
    when(tripMapper.toDto(any())).thenReturn(new TripDTO());

    Pageable pageable = PageRequest.of(0, 10);
    Page<TripDTO> result = tripService.findTripsInPast(pageable);

    assertEquals(1, result.stream().toList().size());
  }

  @Test
  void findTripsInFuture_returnsFilteredTrips() {

    List<Trip> trips = List.of(createTrip(1L));
    Page<Trip> pagedResult = new PageImpl<>(trips);

    when(tripRepository.findByDepartureDateAfter(any(), any(Pageable.class)))
        .thenReturn(pagedResult);
    when(tripMapper.toDto(any())).thenReturn(new TripDTO());

    Pageable pageable = PageRequest.of(0, 10);
    Page<TripDTO> result = tripService.findTripsInFuture(pageable);

    assertEquals(1, result.stream().toList().size());
  }

  @Test
  void findTripsByKeyword_returnsFilteredTrips() {
    String keyword = "trip a";
    List<Trip> trips = List.of(createTrip(1L));
    Page<Trip> pagedResult = new PageImpl<>(trips);

    when(tripRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            eq(keyword), eq(keyword), any(Pageable.class)))
        .thenReturn(pagedResult);
    when(tripMapper.toDto(any())).thenReturn(new TripDTO());

    Pageable pageable = PageRequest.of(0, 10);
    Page<TripDTO> result = tripService.findTripsByKeyword(keyword, pageable);

    assertEquals(1, result.stream().toList().size());
  }

  @Test
  void findOneById_found_returnsDto() {
    Trip trip = createTrip(1L);
    TripDTO dto = createTripDTO(1L);

    when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
    when(tripMapper.toDto(trip)).thenReturn(dto);

    Optional<TripDTO> result = tripService.findOneById(1L);

    assertTrue(result.isPresent());
    assertEquals(1L, result.get().getId());
  }

  @Test
  void createTrip_savesTripAndReturnsDto() {
    TripDTO inputDTO = createTripDTO(null);
    Trip mappedTrip = new Trip();
    Location depLoc = new Location();
    Location arrLoc = new Location();
    User user1 = new User();
    user1.setId(1L);
    User user2 = new User();
    user2.setId(2L);
    Trip savedTrip = createTrip(99L);
    TripDTO resultDTO = createTripDTO(99L);

    when(tripMapper.toEntityWithoutLocations(inputDTO)).thenReturn(mappedTrip);
    when(locationService.findOrCreate(any())).thenReturn(depLoc).thenReturn(arrLoc);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
    when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
    when(tripRepository.save(mappedTrip)).thenReturn(savedTrip);
    when(tripMapper.toDto(savedTrip)).thenReturn(resultDTO);

    TripDTO result = tripService.createTrip(inputDTO);

    assertEquals(99L, result.getId());
    verify(tripRepository).save(mappedTrip);
  }

  @Test
  void updateTrip_updatesTripAndReturnsDto() {
    TripDTO inputDTO = createTripDTO(33L);
    Trip existingTrip = createTrip(33L);
    Location depLoc = new Location();
    Location arrLoc = new Location();
    User user1 = new User();
    user1.setId(1L);
    Trip savedTrip = createTrip(33L);
    TripDTO resultDTO = createTripDTO(33L);

    when(tripRepository.findById(33L)).thenReturn(Optional.of(existingTrip));
    when(locationService.findOrCreate(any())).thenReturn(depLoc).thenReturn(arrLoc);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
    when(tripRepository.save(existingTrip)).thenReturn(savedTrip);
    when(tripMapper.toDto(savedTrip)).thenReturn(resultDTO);

    TripDTO result = tripService.updateTrip(33L, inputDTO);

    assertEquals(33L, result.getId());
    verify(tripRepository).save(existingTrip);
  }

  @Test
  void updateTrip_updatesTripWithNewUsersAndReturnsDto() {
    TripDTO inputDTO = createTripDTO(33L);
    List<Long> userIds = new ArrayList<>();
    userIds.add(1L);
    userIds.add(2L);
    inputDTO.setParticipantIds(userIds);

    Trip existingTrip = createTrip(33L);
    Location depLoc = new Location();
    Location arrLoc = new Location();
    User user1 = new User();
    user1.setId(1L);
    User user2 = new User();
    user2.setId(2L);

    Trip savedTrip = createTrip(33L);
    TripDTO resultDTO = createTripDTO(33L);

    when(tripRepository.findById(33L)).thenReturn(Optional.of(existingTrip));
    when(locationService.findOrCreate(any())).thenReturn(depLoc).thenReturn(arrLoc);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
    when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

    when(tripRepository.save(existingTrip)).thenReturn(savedTrip);
    when(tripMapper.toDto(savedTrip)).thenReturn(resultDTO);

    TripDTO result = tripService.updateTrip(33L, inputDTO);

    assertEquals(33L, result.getId());
    assertEquals(2, result.getParticipantIds().size());
    verify(tripRepository).save(existingTrip);
  }

  @Test
  void updateTrip_throwsIfNotFound() {
    when(tripRepository.findById(404L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> tripService.updateTrip(404L, new TripDTO()));
  }

  @Test
  void deleteTrip_deletesTrip() {
    Trip trip = new Trip();
    trip.setId(1L);
    trip.setParticipants(new ArrayList<>());

    when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

    tripService.deleteTrip(1L);

    verify(tripRepository).deleteById(1L);
  }

  @Test
  void deleteTrip_deletesTripAndCleansUpAssociations() {
    // Create mock participant and trip
    User user = new User();
    Trip trip = new Trip();

    // Set up bidirectional relationship
    trip.setId(1L);
    trip.setParticipants(new ArrayList<>(List.of(user)));
    user.setTrips(new ArrayList<>(List.of(trip)));

    // Mock the repository behavior
    when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
    when(userRepository.save(any(User.class))).thenReturn(user);

    // Call the service method
    tripService.deleteTrip(1L);

    // Verify that participant's trips were modified and saved
    verify(userRepository).save(user);

    // Verify trip was deleted
    verify(tripRepository).deleteById(1L);
  }
}
