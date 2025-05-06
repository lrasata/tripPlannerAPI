package com.lrasata.tripDesignApp.service.mapper;

import com.lrasata.tripDesignApp.entity.Trip;
import com.lrasata.tripDesignApp.service.dto.TripDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TripMapper {

//    @Mappings({
//            @Mapping(target = "budget", source = "trip.budget"),
//            @Mapping(target = "activities", source = "trip.activities"),
//            // @Mapping(target = "participantIds", expression = "java(trip.getParticipants().stream().map(user -> user.getId()).collect(java.util.stream.Collectors.toList()))")
//    })
    TripDTO toDto(Trip trip);

//    @Mappings({
//            @Mapping(target = "budget", source = "tripDTO.budget"),
//            @Mapping(target = "activities", ignore = true), // To be handled separately if needed
//            // @Mapping(target = "participants", ignore = true) // Will be handled separately if needed
//    })
    Trip toEntity(TripDTO tripDTO);

}

