package com.lrasata.tripPlannerAPI.rest.controller;

import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.rest.response.LoginResponse;
import com.lrasata.tripPlannerAPI.service.AuthenticationService;
import com.lrasata.tripPlannerAPI.service.dto.LoginUserDTO;
import com.lrasata.tripPlannerAPI.service.dto.RegisterUserDTO;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
  private static final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);

  private final AuthenticationService authenticationService;

  public AuthenticationController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @PostMapping("/signup")
  public ResponseEntity<?> register(@RequestBody RegisterUserDTO registerUserDto) {
    LOG.debug("REST request to signup : {}", registerUserDto.getEmail());

    Optional<User> registeredUser = authenticationService.signup(registerUserDto);

    if (registeredUser.isPresent()) {
      return ResponseEntity.ok(registeredUser.get());
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User could not be created");
    }
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDTO loginUserDto) {
    LOG.debug("REST request to login : {}", loginUserDto.getEmail());

    return ResponseEntity.ok(authenticationService.login(loginUserDto));
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<?> refresh(@RequestBody Map<String, String> payload) {
    LOG.debug("REST request to refresh token");

    String refreshToken = payload.get("refreshToken");
    return ResponseEntity.ok(
        Map.of("accessToken", authenticationService.refreshAccessToken(refreshToken)));
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(@RequestBody Map<String, String> payload) {
    LOG.debug("REST request to logout");

    String refreshToken = payload.get("refreshToken");
    authenticationService.logout(refreshToken);
    return ResponseEntity.ok("Logged out");
  }
}
