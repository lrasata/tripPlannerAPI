package com.lrasata.tripPlannerAPI.service.mapper;

import com.lrasata.tripPlannerAPI.entity.Trip;
import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.service.dto.UserDTO;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
    componentModel = "spring",
    uses = {RoleMapper.class})
public interface UserMapper {
  @Mapping(target = "password", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "authorities", ignore = true)
  @Mapping(target = "trips", ignore = true)
  User toEntityWithoutTrips(UserDTO dto);

  @Mapping(target = "tripIds", expression = "java(mapTripIds(user.getTrips()))")
  UserDTO toDto(User user);

  default List<Long> mapTripIds(List<Trip> trips) {
    if (trips == null) {
      return Collections.emptyList();
    }
    return trips.stream().map(Trip::getId).collect(Collectors.toList());
  }

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "trips", ignore = true)
  @Mapping(target = "password", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "authorities", ignore = true)
  void updateEntityFromDto(UserDTO userDTO, @MappingTarget User user);
}
