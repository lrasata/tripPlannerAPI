package com.lrasata.tripPlannerAPI.service;

import com.lrasata.tripPlannerAPI.entity.Trip;
import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.repository.TripRepository;
import com.lrasata.tripPlannerAPI.repository.UserRepository;
import com.lrasata.tripPlannerAPI.service.dto.UserDTO;
import com.lrasata.tripPlannerAPI.service.dto.UserProfileDTO;
import com.lrasata.tripPlannerAPI.service.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final TripRepository tripRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(
      UserRepository userRepository,
      UserMapper userMapper,
      TripRepository tripRepository,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.tripRepository = tripRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public UserDTO createUser(UserDTO userDTO) {
    if (userRepository.existsByEmail(userDTO.getEmail())) {
      throw new IllegalArgumentException("Email already exists");
    }
    User user = userMapper.toEntityWithoutTrips(userDTO);
    User saved = userRepository.save(user);
    return userMapper.toDto(saved);
  }

  public List<UserDTO> getAllUsers() {
    return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
  }

  public List<UserDTO> getUsersByIds(List<Long> ids) {
    return userRepository.findAllById(ids).stream()
        .map(userMapper::toDto)
        .collect(Collectors.toList());
  }

  public List<UserDTO> getUsersByEmail(String emailFragment) {
    return userRepository.findByEmailContaining(emailFragment).stream()
        .map(userMapper::toDto)
        .collect(Collectors.toList());
  }

  public UserDTO getUserById(Long id) {
    return userMapper.toDto(
        userRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id)));
  }

  public UserDTO updateUser(Long id, UserDTO userDTO) {
    User existingUser =
        userRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
    userMapper.updateEntityFromDto(userDTO, existingUser);
    return userMapper.toDto(userRepository.save(existingUser));
  }

  @Transactional
  public void deleteUser(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

    // Clean up associations
    for (Trip trip : new ArrayList<>(user.getTrips())) {
      trip.removeParticipant(user);
      tripRepository.save(trip);
    }

    userRepository.deleteById(userId);
  }

  public UserDTO updateUserProfile(String email, UserProfileDTO request) {
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    if (request.getFullName() != null) {
      user.setFullName(request.getFullName());
    }

    if (request.getEmail() != null) {
      user.setEmail(request.getEmail());
    }

    if (request.getPassword() != null && !request.getPassword().isBlank()) {
      user.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    return userMapper.toDto(userRepository.save(user));
  }
}
