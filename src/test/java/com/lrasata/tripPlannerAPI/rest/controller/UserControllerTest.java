package com.lrasata.tripPlannerAPI.rest.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.service.UserService;
import com.lrasata.tripPlannerAPI.service.dto.RoleDTO;
import com.lrasata.tripPlannerAPI.service.dto.UserDTO;
import com.lrasata.tripPlannerAPI.service.dto.UserProfileDTO;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

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
  void authenticatedUser_shouldReturnUnauthorized_whenAnonymousUser() {
    // Mock anonymous authentication
    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(false);
    when(authentication.getPrincipal()).thenReturn("anonymousUser");

    ResponseEntity<UserDTO> response = userController.authenticatedUser(authentication);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  void authenticatedUser_shouldReturnUser_whenAuthenticated() {
    // Mock authenticated user
    User mockUser = new User();
    mockUser.setId(1L);
    mockUser.setEmail("test@example.com");

    UserDTO mockUserDTO = new UserDTO();
    mockUserDTO.setEmail("test@example.com");

    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getPrincipal()).thenReturn(mockUser);

    when(userService.getUserById(1L)).thenReturn(mockUserDTO);

    ResponseEntity<UserDTO> response = userController.authenticatedUser(authentication);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("test@example.com", response.getBody().getEmail());
  }

  @Test
  void updateProfile_shouldUpdateUserProfile() {
    String email = "test@example.com";

    UserProfileDTO profileDTO = new UserProfileDTO();
    profileDTO.setFullName("Jane Doe");

    UserDTO updatedUser = new UserDTO();
    updatedUser.setEmail(email);
    updatedUser.setFullName("Jane Doe");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(email);

    when(userService.updateUserProfile(email, profileDTO)).thenReturn(updatedUser);

    ResponseEntity<UserDTO> response = userController.updateProfile(profileDTO, authentication);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Jane Doe", response.getBody().getFullName());
    assertEquals(email, response.getBody().getEmail());
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
