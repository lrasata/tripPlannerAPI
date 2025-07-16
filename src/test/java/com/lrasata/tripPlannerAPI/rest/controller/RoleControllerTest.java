package com.lrasata.tripPlannerAPI.rest.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.lrasata.tripPlannerAPI.service.RoleService;
import com.lrasata.tripPlannerAPI.service.dto.RoleDTO;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class RoleControllerTest {

  private RoleService roleService;
  private RoleController roleController;

  @BeforeEach
  void setUp() {
    roleService = mock(RoleService.class);
    roleController = new RoleController(roleService);
  }

  @Test
  void getAllRoles_returnsListOfRoles() {
    RoleDTO role1 = new RoleDTO();
    role1.setName("ROLE_ADMIN");

    RoleDTO role2 = new RoleDTO();
    role2.setName("ROLE_USER");

    List<RoleDTO> mockRoles = Arrays.asList(role1, role2);
    when(roleService.findAll()).thenReturn(mockRoles);

    ResponseEntity<List<RoleDTO>> response = roleController.getAllRoles();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, response.getBody().size());
    assertEquals("ROLE_ADMIN", response.getBody().get(0).getName());
    assertEquals("ROLE_USER", response.getBody().get(1).getName());

    verify(roleService, times(1)).findAll();
  }
}
