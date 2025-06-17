package com.lrasata.tripPlannerAPI.service.mapper;

import com.lrasata.tripPlannerAPI.entity.Role;
import com.lrasata.tripPlannerAPI.service.dto.RoleDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

  RoleDTO toDto(Role role);

  Role toEntity(RoleDTO roleDto);
}
