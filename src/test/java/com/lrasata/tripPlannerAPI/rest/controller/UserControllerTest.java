package com.lrasata.tripPlannerAPI.rest.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.lrasata.tripPlannerAPI.service.UserService;
import com.lrasata.tripPlannerAPI.service.dto.RoleDTO;
import com.lrasata.tripPlannerAPI.service.dto.UserDTO;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

class UserControllerTest {

  @InjectMocks private UserController userController;

  @Mock private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private UserDTO createUser(Long id, String name, String email) {
    return new UserDTO(id, name, email, new RoleDTO(), List.of(101L, 102L));
  }

  @Test
  void getAllUsers_noFilter_returnsAllUsers() {
    List<UserDTO> users =
        List.of(
            createUser(1L, "Alice", "alice@example.com"), createUser(2L, "Bob", "bob@example.com"));
    when(userService.getAllUsers()).thenReturn(users);

    ResponseEntity<List<UserDTO>> response = userController.getAllUsers(null, null);

    assertEquals(2, response.getBody().size());
    assertEquals("Alice", response.getBody().get(0).getFullName());
  }

  @Test
  void getAllUsers_withEmailFilter_returnsFilteredUsers() {
    List<UserDTO> filtered = List.of(createUser(3L, "Charlie", "charlie@example.com"));
    when(userService.getUsersByEmail("charlie")).thenReturn(filtered);

    ResponseEntity<List<UserDTO>> response = userController.getAllUsers("charlie", null);

    assertEquals(1, response.getBody().size());
    assertTrue(response.getBody().get(0).getEmail().contains("charlie"));
  }

  //  @Test
  //  void getUserById_returnsUser() {
  //    UserDTO user = createUser(4L, "Diana", "diana@example.com");
  //    when(userService.getUserById(4L)).thenReturn(user);
  //
  //    ResponseEntity<UserDTO> response = userController.getUserById(4L);
  //
  //    assertEquals("Diana", response.getBody().getFullName());
  //    assertEquals(RoleEnum.ROLE_PARTICIPANT, response.getBody().getRole());
  //  }

  @Test
  void createUser_returnsCreatedUser() {
    UserDTO toCreate = createUser(null, "Eve", "eve@example.com");
    UserDTO created = createUser(5L, "Eve", "eve@example.com");

    when(userService.createUser(toCreate)).thenReturn(created);

    ResponseEntity<UserDTO> response = userController.createUser(toCreate);

    assertEquals(5L, response.getBody().getId());
    assertEquals("Eve", response.getBody().getFullName());
  }

  @Test
  void updateUser_returnsUpdatedUser() {
    UserDTO updated = createUser(6L, "Frank", "frank@example.com");
    when(userService.updateUser(6L, updated)).thenReturn(updated);

    ResponseEntity<UserDTO> response = userController.updateUser(6L, updated);

    assertEquals("Frank", response.getBody().getFullName());
    assertEquals("frank@example.com", response.getBody().getEmail());
  }

  @Test
  void deleteUser_returnsNoContent() {
    doNothing().when(userService).deleteUser(7L);

    ResponseEntity<Void> response = userController.deleteUser(7L);

    assertEquals(204, response.getStatusCodeValue());
    verify(userService, times(1)).deleteUser(7L);
  }
}
