package com.lrasata.tripPlannerAPI.service.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.lrasata.tripPlannerAPI.entity.Trip;
import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.service.dto.TripDTO;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class TripMapperTest {

  private final TripMapper tripMapper = Mappers.getMapper(TripMapper.class);

  @Test
  void toDto_shouldMapParticipantIdsAndOtherFields() {
    // Setup entity with participants
    User user1 = new User();
    user1.setId(10L);
    User user2 = new User();
    user2.setId(20L);

    Trip trip = new Trip();
    trip.setId(100L);
    trip.setParticipants(List.of(user1, user2));

    // Map to DTO
    TripDTO dto = tripMapper.toDto(trip);

    assertNotNull(dto);
    assertEquals(100L, dto.getId());
    assertNotNull(dto.getParticipantIds());
    assertEquals(2, dto.getParticipantIds().size());
    assertTrue(dto.getParticipantIds().contains(10L));
    assertTrue(dto.getParticipantIds().contains(20L));
  }

  @Test
  void toDto_shouldReturnEmptyListWhenParticipantsNull() {
    Trip trip = new Trip();
    trip.setParticipants(null);

    List<Long> ids = tripMapper.mapParticipantIds(trip.getParticipants());
    assertNotNull(ids);
    assertTrue(ids.isEmpty());
  }

  @Test
  void toEntityWithoutLocations_shouldIgnoreLocationsAndParticipants() {
    TripDTO dto = new TripDTO();
    dto.setId(200L);

    Trip trip = tripMapper.toEntityWithoutLocations(dto);

    assertNotNull(trip);
    assertEquals(200L, trip.getId());
    assertEquals(0, trip.getParticipants().size());
    assertNull(trip.getDepartureLocation());
    assertNull(trip.getArrivalLocation());
  }
}
