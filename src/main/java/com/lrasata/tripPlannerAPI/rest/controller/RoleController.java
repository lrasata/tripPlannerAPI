package com.lrasata.tripPlannerAPI.rest.controller;

import com.lrasata.tripPlannerAPI.service.RoleService;
import com.lrasata.tripPlannerAPI.service.dto.RoleDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

  private static final Logger LOG = LoggerFactory.getLogger(RoleController.class);

  private final RoleService roleService;

  public RoleController(RoleService roleService) {
    this.roleService = roleService;
  }

  @GetMapping
  public ResponseEntity<List<RoleDTO>> getAllRoles() {
    LOG.debug("REST request to get all Roles");
    List<RoleDTO> roles = roleService.findAll();

    return ResponseEntity.ok().body(roles);
  }
}
