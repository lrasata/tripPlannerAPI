package com.lrasata.tripPlannerAPI.service.mapper;

import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.service.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
  User toEntity(UserDTO dto);

  UserDTO toDto(User entity);

  @Mapping(target = "id", ignore = true)
  void updateEntityFromDto(UserDTO userDTO, @MappingTarget User user);
}
