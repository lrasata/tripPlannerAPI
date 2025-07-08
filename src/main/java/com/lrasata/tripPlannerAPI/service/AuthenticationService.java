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
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationService {
  private static final Logger LOG = LoggerFactory.getLogger(AuthenticationService.class);

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

  @Value("${trip-design-app.cookie.secure-attribute}")
  private Boolean cookieSecureAttribute;

  @Value("${trip-design-app.cookie.same-site}")
  private String cookieSameSite;

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

  public LoginResponse buildLoginResponse(User user) {

    String refreshJwtToken = jwtService.generateRefreshToken(user);
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setToken(refreshJwtToken);
    refreshToken.setUsername(user.getUsername());
    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpirationTime));
    refreshTokenRepository.save(refreshToken);

    String jwtToken = jwtService.generateAccessToken(user);
    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setToken(jwtToken);
    loginResponse.setExpiresIn(accessTokenExpirationTime);
    loginResponse.setRefreshToken(refreshJwtToken);

    return loginResponse;
  }

  @Transactional
  public LoginResponse login(LoginUserDTO loginUserDto) {
    User authenticatedUser = this.authenticate(loginUserDto);

    return buildLoginResponse(authenticatedUser);
  }

  @Transactional
  public LoginResponse refreshTokens(String oldRefreshToken) {
    RefreshToken token =
        refreshTokenRepository
            .findByToken(oldRefreshToken)
            .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

    if (token.getExpiryDate().isBefore(Instant.now())) {
      // delete expired token
      refreshTokenRepository.deleteByToken(oldRefreshToken);
      throw new RuntimeException("Expired refresh token");
    }

    // Find the user
    User user =
        userRepository
            .findByEmail(token.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

    // Delete old refresh token (to prevent reuse)
    try {
      refreshTokenRepository.deleteByToken(oldRefreshToken);
    } catch (Exception e) {
      LOG.warn("Token already deleted by another request: {}", oldRefreshToken);
    }

    return buildLoginResponse(user);
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

  public ResponseCookie setResponseCookie(String accessToken, Duration duration) {
    return ResponseCookie.from("token", accessToken)
        .httpOnly(true)
        .secure(cookieSecureAttribute) // Add the "Secure" attribute to the cookie.
        .sameSite(cookieSameSite) // Lax by default, None if  cross-origin + using credentials (but
        // must come with : secure=true)
        .path("/")
        .maxAge(duration)
        .build();
  }

  public void logout(String refreshToken) {
    refreshTokenRepository.deleteByToken(refreshToken);
  }
}
