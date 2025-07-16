package com.lrasata.tripPlannerAPI.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.lrasata.tripPlannerAPI.entity.Role;
import com.lrasata.tripPlannerAPI.entity.RoleEnum;
import com.lrasata.tripPlannerAPI.repository.RoleRepository;
import com.lrasata.tripPlannerAPI.service.dto.RoleDTO;
import com.lrasata.tripPlannerAPI.service.mapper.RoleMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoleServiceTest {

  private RoleRepository roleRepository;
  private RoleMapper roleMapper;
  private RoleService roleService;

  @BeforeEach
  void setUp() {
    roleRepository = mock(RoleRepository.class);
    roleMapper = mock(RoleMapper.class);
    roleService = new RoleService(roleRepository, roleMapper);
  }

  @Test
  void findAll_shouldReturnMappedRoleDTOs() {
    // Arrange
    Role role1 = new Role();
    role1.setId(1L);
    role1.setName(RoleEnum.ROLE_ADMIN);

    Role role2 = new Role();
    role2.setId(2L);
    role2.setName(RoleEnum.ROLE_PARTICIPANT);

    RoleDTO dto1 = new RoleDTO();
    dto1.setName("ROLE_ADMIN");

    RoleDTO dto2 = new RoleDTO();
    dto2.setName("ROLE_PARTICIPANT");

    when(roleRepository.findAll()).thenReturn(Arrays.asList(role1, role2));
    when(roleMapper.toDto(role1)).thenReturn(dto1);
    when(roleMapper.toDto(role2)).thenReturn(dto2);

    // Act
    List<RoleDTO> result = roleService.findAll();

    // Assert
    assertEquals(2, result.size());
    assertEquals("ROLE_ADMIN", result.get(0).getName());
    assertEquals("ROLE_PARTICIPANT", result.get(1).getName());

    verify(roleRepository, times(1)).findAll();
    verify(roleMapper, times(1)).toDto(role1);
    verify(roleMapper, times(1)).toDto(role2);
  }
}
