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
    List<Trip> trips = List.of(createTrip(1L), createTrip(2L));
    when(tripRepository.findAll()).thenReturn(trips);
    when(tripMapper.toDto(any())).thenReturn(new TripDTO());

    List<TripDTO> result = tripService.findAll();

    assertEquals(2, result.size());
  }

  @Test
  void findTripsInPast_returnsFilteredTrips() {
    List<Trip> trips = List.of(createTrip(1L));
    when(tripRepository.findByDepartureDateBefore(any())).thenReturn(trips);
    when(tripMapper.toDto(any())).thenReturn(new TripDTO());

    List<TripDTO> result = tripService.findTripsInPast();

    assertEquals(1, result.size());
  }

  @Test
  void findTripsInFuture_returnsFilteredTrips() {
    List<Trip> trips = List.of(createTrip(1L));
    when(tripRepository.findByDepartureDateAfter(any())).thenReturn(trips);
    when(tripMapper.toDto(any())).thenReturn(new TripDTO());

    List<TripDTO> result = tripService.findTripsInFuture();

    assertEquals(1, result.size());
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
    Trip mappedTrip = new Trip();
    Location depLoc = new Location();
    Location arrLoc = new Location();
    User user1 = new User();
    user1.setId(1L);
    Trip savedTrip = createTrip(33L);
    TripDTO resultDTO = createTripDTO(33L);

    when(tripRepository.findById(33L)).thenReturn(Optional.of(existingTrip));
    when(tripMapper.toEntityWithoutLocations(inputDTO)).thenReturn(mappedTrip);
    when(locationService.findOrCreate(any())).thenReturn(depLoc).thenReturn(arrLoc);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
    when(userRepository.findById(2L)).thenReturn(Optional.empty()); // simulate one missing user
    when(tripRepository.save(mappedTrip)).thenReturn(savedTrip);
    when(tripMapper.toDto(savedTrip)).thenReturn(resultDTO);

    TripDTO result = tripService.updateTrip(33L, inputDTO);

    assertEquals(33L, result.getId());
    verify(tripRepository).save(mappedTrip);
  }

  @Test
  void updateTrip_throwsIfNotFound() {
    when(tripRepository.findById(404L)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> tripService.updateTrip(404L, new TripDTO()));
  }

  @Test
  void deleteTrip_deletesTrip() {
    doNothing().when(tripRepository).deleteById(1L);

    tripService.deleteTrip(1L);

    verify(tripRepository).deleteById(1L);
  }
}
