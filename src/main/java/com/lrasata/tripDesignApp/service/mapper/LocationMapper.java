package com.lrasata.tripDesignApp.service.mapper;

import com.lrasata.tripDesignApp.entity.Location;
import com.lrasata.tripDesignApp.service.dto.LocationDTO;

public class LocationMapper {

    public static LocationDTO toDto(Location location) {
        if (location == null) return null;

        LocationDTO dto = new LocationDTO();
        dto.setId(location.getId());
        dto.setCity(location.getCity());
        dto.setCountry(location.getCountry());
        dto.setCountryCode(location.getCountryCode());
        return dto;
    }

    public static Location toEntity(LocationDTO dto) {
        if (dto == null) return null;

        Location location = new Location();
        location.setId(dto.getId()); // only if managed by caller; otherwise skip setting ID on create
        location.setCity(dto.getCity());
        location.setCountry(dto.getCountry());
        location.setCountryCode(dto.getCountryCode());
        return location;
    }
}

