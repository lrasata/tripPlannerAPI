package com.lrasata.tripPlannerAPI.service;

import com.lrasata.tripPlannerAPI.entity.RoleEnum;
import com.lrasata.tripPlannerAPI.entity.Trip;
import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.repository.TripRepository;
import com.lrasata.tripPlannerAPI.repository.UserRepository;
import com.lrasata.tripPlannerAPI.service.dto.TripDTO;
import com.lrasata.tripPlannerAPI.service.dto.TripMetadataDTO;
import com.lrasata.tripPlannerAPI.service.mapper.TripMapper;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TripService {

  private final TripRepository tripRepository;
  private final LocationService locationService;
  private final UserRepository userRepository;
  private final TripMapper tripMapper;
  private final TripMetadataService tripMetadataService;

  public TripService(
      TripRepository tripRepository,
      LocationService locationService,
      TripMapper tripMapper,
      UserRepository userRepository,
      TripMetadataService tripMetadataService) {
    this.tripRepository = tripRepository;
    this.locationService = locationService;
    this.tripMapper = tripMapper;
    this.userRepository = userRepository;
    this.tripMetadataService = tripMetadataService;
  }

  public Page<TripDTO> findAll(Pageable pageable) {
    return tripRepository.findAll(pageable).map(tripMapper::toDto);
  }

  public Page<TripDTO> findTripsByParticipant(Long userId, Pageable pageable) {
    Page<Trip> trips = tripRepository.findByParticipants_Id(userId, pageable);
    return trips.map(tripMapper::toDto);
  }

  public Page<TripDTO> findTripsInPast(Pageable pageable) {
    return tripRepository
        .findByDepartureDateBefore(LocalDate.now(), pageable)
        .map(tripMapper::toDto);
  }

  public Page<TripDTO> findTripsByParticipantInPast(Long userId, Pageable pageable) {
    Page<Trip> trips = tripRepository.findByParticipantInPast(userId, pageable);
    return trips.map(tripMapper::toDto);
  }

  public Page<TripDTO> findTripsInFuture(Pageable pageable) {
    return tripRepository
        .findByDepartureDateAfter(LocalDate.now(), pageable)
        .map(tripMapper::toDto);
  }

  public Page<TripDTO> findTripsByParticipantInFuture(Long userId, Pageable pageable) {
    Page<Trip> trips = tripRepository.findByParticipantInFuture(userId, pageable);
    return trips.map(tripMapper::toDto);
  }

  public Page<TripDTO> findTripsByKeyword(String keyword, Pageable pageable) {
    return tripRepository
        .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, pageable)
        .map(tripMapper::toDto);
  }

  public Page<TripDTO> findTripsByParticipantAndKeyword(
      Long userId, String keyword, Pageable pageable) {
    Page<Trip> trips = tripRepository.findByParticipantAndKeyword(userId, keyword, pageable);
    return trips.map(tripMapper::toDto);
  }

  public Optional<TripDTO> findOneById(Long id) {
    return tripRepository
        .findById(id)
        .map(
            trip -> {
              List<TripMetadataDTO> extraInfo = tripMetadataService.getFileByTrip(id);
              return new TripDTO(trip, extraInfo);
            });
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

  public boolean checkIfUserCanAccessTrip(Long tripId, User user) {
    return user.getRole().getName().equals(RoleEnum.ROLE_SUPER_ADMIN)
        || user.getRole().getName().equals(RoleEnum.ROLE_ADMIN)
        || tripRepository.existsByTripIdAndUserId(tripId, user.getId());
  }
}
