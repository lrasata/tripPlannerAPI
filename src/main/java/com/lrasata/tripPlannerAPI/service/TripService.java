package com.lrasata.tripPlannerAPI.service;

import com.lrasata.tripPlannerAPI.entity.Trip;
import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.repository.TripRepository;
import com.lrasata.tripPlannerAPI.repository.UserRepository;
import com.lrasata.tripPlannerAPI.service.dto.TripDTO;
import com.lrasata.tripPlannerAPI.service.mapper.TripMapper;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TripService {

  private static final Logger LOG = LoggerFactory.getLogger(TripService.class);

  private final TripRepository tripRepository;
  private final LocationService locationService;
  private final UserRepository userRepository;
  private final TripMapper tripMapper;

  public TripService(
      TripRepository tripRepository,
      LocationService locationService,
      TripMapper tripMapper,
      UserRepository userRepository) {
    this.tripRepository = tripRepository;
    this.locationService = locationService;
    this.tripMapper = tripMapper;
    this.userRepository = userRepository;
  }

  public List<TripDTO> findAll() {
    return tripRepository.findAll().stream().map(tripMapper::toDto).collect(Collectors.toList());
  }

  public List<TripDTO> findTripsInPast() {
    return tripRepository.findByDepartureDateBefore(LocalDate.now()).stream()
        .map(tripMapper::toDto)
        .collect(Collectors.toList());
  }

  public List<TripDTO> findTripsInFuture() {
    return tripRepository.findByDepartureDateAfter(LocalDate.now()).stream()
        .map(tripMapper::toDto)
        .collect(Collectors.toList());
  }

  public Optional<TripDTO> findOneById(Long id) {
    return tripRepository.findById(id).map(tripMapper::toDto);
  }

  public TripDTO createTrip(TripDTO dto) {
    Trip trip = tripMapper.toEntityWithoutLocations(dto);

    if (dto.getDepartureLocation() != null) {
      trip.setDepartureLocation(locationService.findOrCreate(dto.getDepartureLocation()));
    }
    if (dto.getArrivalLocation() != null) {
      trip.setArrivalLocation(locationService.findOrCreate(dto.getArrivalLocation()));
    }

    // handle participants
    List<Optional<User>> users =
        dto.getParticipantIds().stream().map(userRepository::findById).toList();
    for (Optional<User> user : users) {
      user.ifPresent(trip::addParticipant);
    }

    // handle participant count
    Integer count = dto.getParticipantCount();
    if (count != null && count < users.size()) {
      trip.setParticipantCount(users.size());
    }

    Trip savedTrip = tripRepository.save(trip);

    return tripMapper.toDto(savedTrip);
  }

  public TripDTO updateTrip(Long id, TripDTO dto) {
    Trip existingTrip =
        tripRepository.findById(id).orElseThrow(() -> new RuntimeException("Trip not found"));

    tripMapper.updateTripFromDto(dto, existingTrip);

    if (dto.getDepartureLocation() != null) {
      existingTrip.setDepartureLocation(locationService.findOrCreate(dto.getDepartureLocation()));
    }
    if (dto.getArrivalLocation() != null) {
      existingTrip.setArrivalLocation(locationService.findOrCreate(dto.getArrivalLocation()));
    }

    // handle participants
    List<User> users =
        dto.getParticipantIds().stream()
            .map(userRepository::findById)
            .filter(Optional::isPresent) // Filter out users not found
            .map(Optional::get) // Extract the User object
            .collect(Collectors.toList());
    existingTrip.setParticipants(users);

    // handle participant count
    Integer count = dto.getParticipantCount();
    if (count == null || count < users.size()) {
      existingTrip.setParticipantCount(users.size());
    }

    Trip savedTrip = tripRepository.save(existingTrip);

    return tripMapper.toDto(savedTrip);
  }

  @Transactional
  public void deleteTrip(Long id) {
    Trip existingTrip =
        tripRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Course not found"));

    for (User user : new HashSet<>(existingTrip.getParticipants())) {
      user.removeTrip(existingTrip);
      userRepository.save(user);
    }

    tripRepository.deleteById(id);
  }
}
