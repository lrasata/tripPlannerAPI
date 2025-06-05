package com.lrasata.tripDesignApp.service.mapper;

import com.lrasata.tripDesignApp.entity.User;
import com.lrasata.tripDesignApp.service.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
  User toEntity(UserDTO dto);

  UserDTO toDto(User entity);

  void updateEntityFromDto(UserDTO userDTO, @MappingTarget User user);
}
