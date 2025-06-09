package com.lrasata.tripPlannerAPI.rest.controller;

import com.lrasata.tripPlannerAPI.service.UserService;
import com.lrasata.tripPlannerAPI.service.dto.UserDTO;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

  public UserController(UserService userService) {
    this.userService = userService;
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
