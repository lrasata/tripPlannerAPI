package com.lrasata.tripDesignApp.service;

import com.lrasata.tripDesignApp.entity.Location;
import com.lrasata.tripDesignApp.repository.LocationRepository;
import com.lrasata.tripDesignApp.service.dto.LocationDTO;
import com.lrasata.tripDesignApp.service.mapper.LocationMapper;
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
