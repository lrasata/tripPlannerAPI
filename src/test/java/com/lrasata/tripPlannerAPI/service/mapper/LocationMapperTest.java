package com.lrasata.tripPlannerAPI.service.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.lrasata.tripPlannerAPI.entity.Location;
import com.lrasata.tripPlannerAPI.service.dto.LocationDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class LocationMapperTest {

  private final LocationMapper locationMapper = Mappers.getMapper(LocationMapper.class);

  @Test
  void toEntity_shouldMapFieldsCorrectly_andIgnoreId() {
    LocationDTO dto = new LocationDTO();
    dto.setCity("Rome");
    dto.setCountryCode("IT");

    Location entity = locationMapper.toEntity(dto);

    assertNotNull(entity);
    assertNull(entity.getId()); // ID should be ignored
    assertEquals("Rome", entity.getCity());
    assertEquals("IT", entity.getCountryCode());
  }

  @Test
  void toDto_shouldMapFieldsCorrectly() {
    Location entity = new Location();
    entity.setId(1L);
    entity.setCity("Madrid");
    entity.setCountryCode("ES");

    LocationDTO dto = locationMapper.toDto(entity);

    assertNotNull(dto);
    assertEquals("Madrid", dto.getCity());
    assertEquals("ES", dto.getCountryCode());
  }
}
