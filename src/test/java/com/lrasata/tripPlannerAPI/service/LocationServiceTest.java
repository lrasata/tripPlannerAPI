package com.lrasata.tripPlannerAPI.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.lrasata.tripPlannerAPI.entity.Location;
import com.lrasata.tripPlannerAPI.repository.LocationRepository;
import com.lrasata.tripPlannerAPI.service.dto.LocationDTO;
import com.lrasata.tripPlannerAPI.service.mapper.LocationMapper;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class LocationServiceTest {

  @Mock private LocationRepository locationRepository;

  @Mock private LocationMapper locationMapper;

  @InjectMocks private LocationService locationService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  private LocationDTO createLocationDTO() {
    LocationDTO dto = new LocationDTO();
    dto.setCity("Paris");
    dto.setCountry("FR");
    return dto;
  }

  private Location createLocation() {
    Location location = new Location();
    location.setId(1L);
    location.setCity("Paris");
    location.setCountryCode("FR");
    return location;
  }

  @Test
  void findOrCreate_whenLocationExists_returnsExistingLocation() {
    LocationDTO dto = createLocationDTO();
    Location location = createLocation();

    when(locationRepository.findByCityAndCountryCode("Paris", "FR"))
        .thenReturn(Optional.of(location));

    Location result = locationService.findOrCreate(dto);

    assertNotNull(result);
    assertEquals("Paris", result.getCity());
    verify(locationRepository, never()).save(any());
  }

  @Test
  void findOrCreate_whenLocationDoesNotExist_createsNewLocation() {
    LocationDTO dto = createLocationDTO();
    Location location = createLocation();

    when(locationRepository.findByCityAndCountryCode("Paris", "FR")).thenReturn(Optional.empty());
    when(locationMapper.toEntity(dto)).thenReturn(location);
    when(locationRepository.save(location)).thenReturn(location);

    Location result = locationService.findOrCreate(dto);

    assertNotNull(result);
    assertEquals("Paris", result.getCity());
    verify(locationRepository).save(location);
  }
}
