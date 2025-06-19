package com.lrasata.tripPlannerAPI.service;

import com.lrasata.tripPlannerAPI.entity.RefreshToken;
import com.lrasata.tripPlannerAPI.entity.Role;
import com.lrasata.tripPlannerAPI.entity.RoleEnum;
import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.repository.RefreshTokenRepository;
import com.lrasata.tripPlannerAPI.repository.RoleRepository;
import com.lrasata.tripPlannerAPI.repository.UserRepository;
import com.lrasata.tripPlannerAPI.rest.response.LoginResponse;
import com.lrasata.tripPlannerAPI.service.dto.LoginUserDTO;
import com.lrasata.tripPlannerAPI.service.dto.RegisterUserDTO;
import java.time.Instant;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final PasswordEncoder passwordEncoder;

  private final AuthenticationManager authenticationManager;

  private final RefreshTokenRepository refreshTokenRepository;

  private final JwtService jwtService;

  @Value("${security.jwt.access-token.expiration}")
  private long accessTokenExpirationTime;

  @Value("${security.jwt.refresh-token.expiration}")
  private long refreshTokenExpirationTime;

  public AuthenticationService(
      UserRepository userRepository,
      RoleRepository roleRepository,
      AuthenticationManager authenticationManager,
      PasswordEncoder passwordEncoder,
      RefreshTokenRepository refreshTokenRepository,
      JwtService jwtService) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.refreshTokenRepository = refreshTokenRepository;
    this.jwtService = jwtService;
  }

  public Optional<User> signup(RegisterUserDTO registerUserDTO) {
    if (userRepository.existsByEmail(registerUserDTO.getEmail())) {
      return Optional.empty();
    }

    Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ROLE_PARTICIPANT);
    if (optionalRole.isEmpty()) {
      return Optional.empty();
    }

    User user = new User();

    user.setFullName(registerUserDTO.getFullName());
    user.setEmail(registerUserDTO.getEmail());
    user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
    user.setRole(optionalRole.get());

    return Optional.of(userRepository.save(user));
  }

  public User authenticate(LoginUserDTO loginUserDTO) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginUserDTO.getEmail(), loginUserDTO.getPassword()));

    return userRepository.findByEmail(loginUserDTO.getEmail()).orElseThrow();
  }

  public LoginResponse login(LoginUserDTO loginUserDto) {
    User authenticatedUser = this.authenticate(loginUserDto);

    String refreshJwtToken = jwtService.generateRefreshToken(authenticatedUser);
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setToken(refreshJwtToken);
    refreshToken.setUsername(authenticatedUser.getUsername());
    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpirationTime));
    refreshTokenRepository.save(refreshToken);

    String jwtToken = jwtService.generateAccessToken(authenticatedUser);
    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setToken(jwtToken);
    loginResponse.setExpiresIn(accessTokenExpirationTime);
    loginResponse.setRefreshToken(refreshJwtToken);

    return loginResponse;
  }

  public User createAdministrator(RegisterUserDTO registerUserDTO) {
    Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ROLE_SUPER_ADMIN);

    if (optionalRole.isEmpty()) {
      return null;
    }

    User user = new User();
    user.setFullName(registerUserDTO.getFullName());
    user.setEmail(registerUserDTO.getEmail());
    user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
    user.setRole(optionalRole.get());

    return userRepository.save(user);
  }

  public String refreshAccessToken(String refreshToken) {
    RefreshToken token =
        refreshTokenRepository
            .findByToken(refreshToken)
            .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

    if (token.getExpiryDate().isBefore(Instant.now())) {
      refreshTokenRepository.deleteByToken(refreshToken);
      throw new RuntimeException("Expired refresh token");
    }

    User user = userRepository.findByEmail(token.getUsername()).orElseThrow();

    return jwtService.generateAccessToken(user);
  }

  public void logout(String refreshToken) {
    refreshTokenRepository.deleteByToken(refreshToken);
  }
}
