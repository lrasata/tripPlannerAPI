package com.lrasata.tripPlannerAPI.service.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.lrasata.tripPlannerAPI.entity.Role;
import com.lrasata.tripPlannerAPI.entity.RoleEnum;
import com.lrasata.tripPlannerAPI.service.dto.RoleDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class RoleMapperTest {

  private RoleMapper roleMapper;

  @BeforeEach
  void setUp() {
    roleMapper = Mappers.getMapper(RoleMapper.class);
  }

  @Test
  void testToDto() {
    Role role = new Role();
    role.setName(RoleEnum.ROLE_ADMIN);
    role.setDescription("Admin role");

    RoleDTO dto = roleMapper.toDto(role);

    assertNotNull(dto);
    assertEquals("ROLE_ADMIN", dto.getName());
  }

  @Test
  void testToEntity() {
    RoleDTO dto = new RoleDTO();
    dto.setName("ROLE_PARTICIPANT");

    Role role = roleMapper.toEntity(dto);

    assertNotNull(role);
    assertNull(role.getId()); // id is ignored
    assertEquals("Participant", role.getName().getLabel());
    assertNull(role.getDescription()); // ignored
    assertNull(role.getCreatedAt()); // ignored
    assertNull(role.getUpdatedAt()); // ignored
  }
}
