package com.lrasata.tripPlannerAPI.service.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.lrasata.tripPlannerAPI.entity.Role;
import com.lrasata.tripPlannerAPI.entity.RoleEnum;
import com.lrasata.tripPlannerAPI.entity.Trip;
import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.service.dto.RoleDTO;
import com.lrasata.tripPlannerAPI.service.dto.UserDTO;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Disabled("Skipping all tests in this class temporarily")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserMapperTest {

  @Autowired private UserMapper userMapper;

  @Test
  void testToDtoMapsRole() {
    Role role = new Role();
    role.setId(1L);
    role.setName(RoleEnum.ROLE_ADMIN);

    User user = new User();
    user.setFullName("Admin User");
    user.setEmail("admin@example.com");
    user.setRole(role);

    UserDTO dto = userMapper.toDto(user);

    assertNotNull(dto.getRole());
    assertEquals("ROLE_ADMIN", dto.getRole().getName());
  }

  @Test
  void testToEntityWithoutTripsMapsRole() {
    RoleDTO roleDTO = new RoleDTO();
    roleDTO.setName("ROLE_PARTICIPANT");

    UserDTO dto = new UserDTO();
    dto.setId(10L);
    dto.setFullName("John Doe");
    dto.setEmail("john@example.com");
    dto.setRole(roleDTO);

    User user = userMapper.toEntityWithoutTrips(dto);

    assertEquals(10L, user.getId());
    assertEquals("John Doe", user.getFullName());
    assertEquals("john@example.com", user.getEmail());
    assertEquals(0, user.getTrips().size());
    assertNotNull(user.getRole());
    assertEquals("Participant", user.getRole().getName().getLabel());
    assertNull(user.getPassword());
    assertNull(user.getCreatedAt());
    assertNull(user.getUpdatedAt());
  }

  @Test
  void testToDtoWithTrips() {
    Trip trip1 = new Trip();
    trip1.setId(1L);
    Trip trip2 = new Trip();
    trip2.setId(2L);

    User user = new User();
    user.setId(100L);
    user.setFullName("Jane Doe");
    user.setEmail("jane@example.com");
    user.setTrips(Arrays.asList(trip1, trip2));

    UserDTO dto = userMapper.toDto(user);

    assertEquals("Jane Doe", dto.getFullName());
    assertEquals("jane@example.com", dto.getEmail());
    assertNotNull(dto.getTripIds());
    assertEquals(2, dto.getTripIds().size());
    assertTrue(dto.getTripIds().containsAll(List.of(1L, 2L)));
  }

  @Test
  void testUpdateEntityFromDto() {
    UserDTO dto = new UserDTO();
    dto.setFullName("Updated User");
    dto.setEmail("updated@example.com");

    User user = new User();
    user.setId(5L);
    user.setFullName("Old Name");
    user.setEmail("old@example.com");

    userMapper.updateEntityFromDto(dto, user);

    assertEquals(5L, user.getId()); // not updated
    assertEquals("Updated User", user.getFullName());
    assertEquals("updated@example.com", user.getEmail());
    assertEquals(0, user.getTrips().size());
  }
}
