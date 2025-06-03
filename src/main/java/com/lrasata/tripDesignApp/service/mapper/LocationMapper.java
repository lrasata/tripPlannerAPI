package com.lrasata.tripDesignApp.service.mapper;

import com.lrasata.tripDesignApp.entity.Location;
import com.lrasata.tripDesignApp.service.dto.LocationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(target = "id", ignore = true)
    Location toEntity(LocationDTO dto);

    LocationDTO toDto(Location entity);
}


