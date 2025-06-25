package com.lrasata.tripPlannerAPI.service.mapper;

import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.service.dto.UserProfileDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
  UserProfileDTO toDto(User user);
}
