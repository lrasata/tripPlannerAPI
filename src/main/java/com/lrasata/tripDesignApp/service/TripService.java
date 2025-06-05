package com.lrasata.tripDesignApp.service;

import com.lrasata.tripDesignApp.entity.Trip;
import com.lrasata.tripDesignApp.entity.User;
import com.lrasata.tripDesignApp.repository.TripRepository;
import com.lrasata.tripDesignApp.repository.UserRepository;
import com.lrasata.tripDesignApp.service.dto.TripDTO;
import com.lrasata.tripDesignApp.service.mapper.TripMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    LOG.debug("Request to get all trips");
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
    LOG.debug("Request to get Trip : {}", id);
    return tripRepository.findById(id).map(tripMapper::toDto);
  }

  public TripDTO createTrip(TripDTO dto) {
    Trip trip = tripMapper.toEntityWithoutLocations(dto);

    // Manually handle the location mapping to avoid detached issues
    trip.setDepartureLocation(locationService.findOrCreate(dto.getDepartureLocation()));
    trip.setArrivalLocation(locationService.findOrCreate(dto.getArrivalLocation()));

    // handle participants
    List<Optional<User>> users =
        dto.getParticipantIds().stream().map(userRepository::findById).toList();
    for (Optional<User> user : users) {
      user.ifPresent(trip::addParticipant);
    }

    Trip savedTrip = tripRepository.save(trip);

    return tripMapper.toDto(savedTrip);
  }

  public TripDTO updateTrip(Long id, TripDTO dto) {
    LOG.debug("Request to update Trip : {}", dto);
    tripRepository.findById(id).orElseThrow(() -> new RuntimeException("Trip not found"));

    Trip trip = tripMapper.toEntityWithoutLocations(dto);

    trip.setDepartureLocation(locationService.findOrCreate(dto.getDepartureLocation()));
    trip.setArrivalLocation(locationService.findOrCreate(dto.getArrivalLocation()));

    // handle participants
    List<Optional<User>> users =
        dto.getParticipantIds().stream().map(userRepository::findById).toList();
    for (Optional<User> user : users) {
      user.ifPresent(trip::addParticipant);
    }

    Trip savedTrip = tripRepository.save(trip);

    return tripMapper.toDto(savedTrip);
  }

  public void deleteTrip(Long id) {
    tripRepository.deleteById(id);
  }
}
