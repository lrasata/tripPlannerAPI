package com.lrasata.tripPlannerAPI.rest.controller;

import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.service.AuthenticationService;
import com.lrasata.tripPlannerAPI.service.dto.RegisterUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admins")
@RestController
public class AdminController {
  private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);
  private final AuthenticationService authenticationService;

  public AdminController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @PostMapping
  @PreAuthorize("hasRole('SUPER_ADMIN')")
  public ResponseEntity<User> createAdministrator(@RequestBody RegisterUserDTO registerUserDto) {
    LOG.info("REST request to create Administrator : {}", registerUserDto);

    User createdAdmin = authenticationService.createAdministrator(registerUserDto);

    return ResponseEntity.ok(createdAdmin);
  }
}
