package com.lrasata.tripPlannerAPI.service;

import com.lrasata.tripPlannerAPI.entity.Location;
import com.lrasata.tripPlannerAPI.repository.LocationRepository;
import com.lrasata.tripPlannerAPI.service.dto.LocationDTO;
import com.lrasata.tripPlannerAPI.service.mapper.LocationMapper;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
  private final LocationRepository locationRepository;
  private final LocationMapper locationMapper;

  public LocationService(LocationRepository locationRepository, LocationMapper locationMapper) {
    this.locationRepository = locationRepository;
    this.locationMapper = locationMapper;
  }

  public Location findOrCreate(LocationDTO dto) {
    return locationRepository
        .findByCityAndCountryCode(dto.getCity(), dto.getCountry())
        .orElseGet(() -> locationRepository.save(locationMapper.toEntity(dto)));
  }
}
