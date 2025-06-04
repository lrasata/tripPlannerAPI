package com.lrasata.tripDesignApp.service;

import com.lrasata.tripDesignApp.entity.User;
import com.lrasata.tripDesignApp.repository.UserRepository;
import com.lrasata.tripDesignApp.service.dto.UserDTO;
import com.lrasata.tripDesignApp.service.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = userMapper.toEntity(userDTO);
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        return userMapper.toDto(
                userRepository.findById(id).orElseThrow(() ->
                        new EntityNotFoundException("User not found with id " + id))
        );
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
        userMapper.updateEntityFromDto(userDTO, existingUser);
        return userMapper.toDto(userRepository.save(existingUser));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id " + id);
        }
        userRepository.deleteById(id);
    }
}
