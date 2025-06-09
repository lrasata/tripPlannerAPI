package com.lrasata.tripPlannerAPI.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.lrasata.tripPlannerAPI.entity.Trip;
import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.repository.TripRepository;
import com.lrasata.tripPlannerAPI.repository.UserRepository;
import com.lrasata.tripPlannerAPI.service.dto.UserDTO;
import com.lrasata.tripPlannerAPI.service.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class UserServiceTest {

  @Mock private TripRepository tripRepository;

  @Mock private UserRepository userRepository;

  @Mock private UserMapper userMapper;

  @InjectMocks private UserService userService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  private UserDTO createUserDTO(Long id, String email) {
    UserDTO dto = new UserDTO();
    dto.setId(id);
    dto.setName("John Doe");
    dto.setEmail(email);
    return dto;
  }

  private User createUser(Long id, String email) {
    User user = new User();
    user.setId(id);
    user.setName("John Doe");
    user.setEmail(email);
    return user;
  }

  @Test
  void createUser_whenEmailDoesNotExist_savesUser() {
    UserDTO dto = createUserDTO(null, "john@example.com");
    User user = createUser(null, "john@example.com");
    User savedUser = createUser(1L, "john@example.com");
    UserDTO savedDto = createUserDTO(1L, "john@example.com");

    when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
    when(userMapper.toEntity(dto)).thenReturn(user);
    when(userRepository.save(user)).thenReturn(savedUser);
    when(userMapper.toDto(savedUser)).thenReturn(savedDto);

    UserDTO result = userService.createUser(dto);

    assertNotNull(result);
    assertEquals(1L, result.getId());
  }

  @Test
  void createUser_whenEmailExists_throwsException() {
    UserDTO dto = createUserDTO(null, "john@example.com");
    when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

    assertThrows(IllegalArgumentException.class, () -> userService.createUser(dto));
  }

  @Test
  void getAllUsers_returnsListOfUserDTOs() {
    User user = createUser(1L, "john@example.com");
    UserDTO dto = createUserDTO(1L, "john@example.com");

    when(userRepository.findAll()).thenReturn(List.of(user));
    when(userMapper.toDto(user)).thenReturn(dto);

    List<UserDTO> result = userService.getAllUsers();

    assertEquals(1, result.size());
    assertEquals("john@example.com", result.get(0).getEmail());
  }

  @Test
  void getUsersByEmail_returnsFilteredUsers() {
    User user = createUser(1L, "john@example.com");
    UserDTO dto = createUserDTO(1L, "john@example.com");

    when(userRepository.findByEmailContaining("john")).thenReturn(List.of(user));
    when(userMapper.toDto(user)).thenReturn(dto);

    List<UserDTO> result = userService.getUsersByEmail("john");

    assertEquals(1, result.size());
    assertEquals("john@example.com", result.get(0).getEmail());
  }

  @Test
  void getUserById_whenExists_returnsDTO() {
    User user = createUser(1L, "john@example.com");
    UserDTO dto = createUserDTO(1L, "john@example.com");

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userMapper.toDto(user)).thenReturn(dto);

    UserDTO result = userService.getUserById(1L);

    assertEquals(1L, result.getId());
  }

  @Test
  void getUserById_whenNotExists_throwsException() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1L));
  }

  @Test
  void updateUser_whenUserExists_updatesAndReturnsDTO() {
    User existingUser = createUser(1L, "john@example.com");
    UserDTO inputDTO = createUserDTO(1L, "john@example.com");
    UserDTO updatedDTO = createUserDTO(1L, "john@example.com");

    when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
    doNothing().when(userMapper).updateEntityFromDto(inputDTO, existingUser);
    when(userRepository.save(existingUser)).thenReturn(existingUser);
    when(userMapper.toDto(existingUser)).thenReturn(updatedDTO);

    UserDTO result = userService.updateUser(1L, inputDTO);

    assertEquals(1L, result.getId());
  }

  @Test
  void updateUser_whenNotFound_throwsException() {
    UserDTO dto = createUserDTO(1L, "john@example.com");

    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> userService.updateUser(1L, dto));
  }

  @Test
  void deleteUser_whenUserExists_deletesUser() {
    User user = new User();
    user.setId(1L);
    user.setTrips(new ArrayList<>());

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    userService.deleteUser(1L);

    verify(userRepository).deleteById(1L);
  }

  @Test
  void deleteUser_whenUserDoesNotExist_throwsException() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(1L));
  }

  @Test
  void deleteUser_deletesUserAndCleansUpAssociations() {
    // Create mock participant and trip
    User user = new User();
    Trip trip = new Trip();

    // Set up bidirectional relationship
    user.setId(1L);
    trip.setParticipants(new ArrayList<>(List.of(user)));
    user.setTrips(new ArrayList<>(List.of(trip)));

    // Mock the repository behavior
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(tripRepository.save(any(Trip.class))).thenReturn(trip);

    userService.deleteUser(1L);

    verify(tripRepository).save(trip);

    // Verify trip was deleted
    verify(userRepository).deleteById(1L);
  }
}
