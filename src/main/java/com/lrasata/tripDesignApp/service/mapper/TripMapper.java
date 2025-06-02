package com.lrasata.tripDesignApp.service.mapper;

import com.lrasata.tripDesignApp.entity.Location;
import com.lrasata.tripDesignApp.entity.Trip;
import com.lrasata.tripDesignApp.service.dto.TripDTO;
import org.springframework.stereotype.Component;

@Component
public class TripMapper {

    public TripDTO toDto(Trip trip) {
        if (trip == null) return null;

        TripDTO dto = new TripDTO();
        dto.setId(trip.getId());
        dto.setName(trip.getName());
        dto.setDescription(trip.getDescription());
        dto.setDepartureDate(trip.getDepartureDate());
        dto.setReturnDate(trip.getReturnDate());

        dto.setDepartureLocation(LocationMapper.toDto(trip.getDepartureLocation()));
        dto.setArrivalLocation(LocationMapper.toDto(trip.getArrivalLocation()));

        return dto;
    }

    public Trip toEntity(TripDTO dto, Location departureLocation, Location arrivalLocation) {
        if (dto == null) return null;

        Trip trip = new Trip();
        trip.setId(dto.getId());
        trip.setName(dto.getName());
        trip.setDescription(dto.getDescription());
        trip.setDepartureDate(dto.getDepartureDate());
        trip.setReturnDate(dto.getReturnDate());

        trip.setDepartureLocation(departureLocation);
        trip.setArrivalLocation(arrivalLocation);

        return trip;
    }
}