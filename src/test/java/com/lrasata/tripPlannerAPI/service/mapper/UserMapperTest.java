package com.lrasata.tripPlannerAPI.service.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.service.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class UserMapperTest {

  private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

  @Test
  void toEntity_shouldMapAllFields() {
    UserDTO dto = new UserDTO();
    dto.setId(1L);
    dto.setFullName("Alice");
    dto.setEmail("alice@example.com");
    // dto.setRole(RoleEnum.ROLE_PARTICIPANT);
    dto.setTripIds(null);

    User user = userMapper.toEntityWithoutTrips(dto);

    assertNotNull(user);
    assertEquals(dto.getId(), user.getId());
    assertEquals(dto.getFullName(), user.getFullName());
    assertEquals(dto.getEmail(), user.getEmail());
    assertEquals(dto.getRole(), user.getRole());
  }

  @Test
  void toDto_shouldMapAllFields() {
    User user = new User();
    user.setId(2L);
    user.setFullName("Bob");
    user.setEmail("bob@example.com");
    // user.setRole(RoleEnum.ROLE_ADMIN);

    UserDTO dto = userMapper.toDto(user);

    assertNotNull(dto);
    assertEquals(user.getId(), dto.getId());
    assertEquals(user.getFullName(), dto.getFullName());
    assertEquals(user.getEmail(), dto.getEmail());
    assertEquals(user.getRole(), dto.getRole());
  }

  @Test
  void updateEntityFromDto_shouldUpdateFields() {
    User user = new User();
    user.setId(3L);
    user.setFullName("Charlie");
    user.setEmail("charlie@example.com");
    // user.setRole(RoleEnum.ROLE_PARTICIPANT);

    UserDTO dto = new UserDTO();
    dto.setFullName("Charles");
    dto.setEmail("charles@example.com");
    // dto.setRole(RoleEnum.ROLE_ADMIN);

    userMapper.updateEntityFromDto(dto, user);

    assertEquals("Charles", user.getFullName());
    assertEquals("charles@example.com", user.getEmail());
    // assertEquals(RoleEnum.ROLE_ADMIN, user.getRole());
    assertEquals(3L, user.getId()); // Id remains unchanged
  }
}
