package com.lrasata.tripPlannerAPI.service.mapper;

import com.lrasata.tripPlannerAPI.entity.Trip;
import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.service.dto.TripDTO;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TripMapper {
  @Mapping(
      target = "participantIds",
      expression = "java(mapParticipantIds(trip.getParticipants()))")
  TripDTO toDto(Trip trip);

  default List<Long> mapParticipantIds(List<User> participants) {
    if (participants == null) {
      return Collections.emptyList();
    }
    return participants.stream().map(User::getId).collect(Collectors.toList());
  }

  @Mapping(target = "departureLocation", ignore = true)
  @Mapping(target = "arrivalLocation", ignore = true)
  @Mapping(target = "participants", ignore = true)
  Trip toEntityWithoutLocations(TripDTO dto);
}
