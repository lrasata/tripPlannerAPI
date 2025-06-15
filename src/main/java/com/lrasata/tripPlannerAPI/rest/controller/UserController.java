package com.lrasata.tripPlannerAPI.rest.controller;

import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.service.UserService;
import com.lrasata.tripPlannerAPI.service.dto.UserDTO;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/me")
  public ResponseEntity<UserDTO> authenticatedUser() {
    LOG.debug("REST request to get current user");
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null
        || !authentication.isAuthenticated()
        || authentication.getPrincipal().equals("anonymousUser")) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    User currentUser = (User) authentication.getPrincipal();
    return ResponseEntity.ok(userService.getUserById(currentUser.getId()));
  }

  @GetMapping
  public ResponseEntity<List<UserDTO>> getAllUsers(
      @RequestParam(required = false) String emailContains,
      @RequestParam(required = false) List<Long> ids) {
    LOG.debug("REST request to get all Users with filter: {}", emailContains);
    if (emailContains != null && !emailContains.isEmpty()) {
      return ResponseEntity.ok(userService.getUsersByEmail(emailContains));
    }

    // Accept a comma-separated list of IDs in 'ids' query param
    if (ids != null && !ids.isEmpty()) {
      return ResponseEntity.ok(userService.getUsersByIds(ids));
    }
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
    LOG.debug("REST request to get User : {}", id);
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @PostMapping
  public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
    LOG.debug("REST request to create User : {}", userDTO);
    return ResponseEntity.ok(userService.createUser(userDTO));
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserDTO> updateUser(
      @PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
    LOG.debug("REST request to update User : {}, {}", id, userDTO);
    return ResponseEntity.ok(userService.updateUser(id, userDTO));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    LOG.debug("REST request to delete User : {}", id);
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
