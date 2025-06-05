package com.lrasata.tripDesignApp.service.mapper;

import com.lrasata.tripDesignApp.entity.Trip;
import com.lrasata.tripDesignApp.service.dto.TripDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TripMapper {

  @Mapping(target = "departureLocation", ignore = true)
  @Mapping(target = "arrivalLocation", ignore = true)
  @Mapping(target = "participants", ignore = true)
  Trip toEntityWithoutLocations(TripDTO dto);

  TripDTO toDto(Trip entity);
}
