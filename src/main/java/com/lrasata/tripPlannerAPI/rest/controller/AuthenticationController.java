package com.lrasata.tripPlannerAPI.rest.controller;

import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.rest.response.LoginResponse;
import com.lrasata.tripPlannerAPI.service.AuthenticationService;
import com.lrasata.tripPlannerAPI.service.dto.LoginUserDTO;
import com.lrasata.tripPlannerAPI.service.dto.RegisterUserDTO;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
  @Value("${trip-design-app.cookie.secure-attribute}")
  private Boolean cookieSecureAttribute;

  @Value("${trip-design-app.cookie.same-site}")
  private String cookieSameSite;

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
  public ResponseEntity<LoginResponse> authenticate(
      @RequestBody LoginUserDTO loginUserDto, HttpServletResponse response) {
    LOG.debug("REST request to login : {}", loginUserDto.getEmail());

    LoginResponse loginResponse = authenticationService.login(loginUserDto);
    String jwtToken = loginResponse.getToken(); // Make sure your LoginResponse contains the token

    ResponseCookie cookie = authenticationService.setResponseCookie(jwtToken, Duration.ofDays(1));
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    return ResponseEntity.ok(loginResponse);
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<?> refresh(
      @RequestBody Map<String, String> payload, HttpServletResponse response) {
    LOG.debug("REST request to refresh token");
    String refreshToken = payload.get("refreshToken");
    LoginResponse tokens = authenticationService.refreshTokens(refreshToken);
    String newAccessToken = tokens.getToken();

    ResponseCookie cookie =
        authenticationService.setResponseCookie(newAccessToken, Duration.ofDays(1));
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    return ResponseEntity.ok(tokens);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletResponse response) {
    ResponseCookie cookie =
        authenticationService.setResponseCookie(
            "", Duration.ofDays(0)); // cookie token expires immediately

    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    return ResponseEntity.ok().build();
  }
}
