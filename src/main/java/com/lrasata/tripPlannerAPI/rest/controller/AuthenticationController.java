package com.lrasata.tripPlannerAPI.rest.controller;

import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.rest.response.LoginResponse;
import com.lrasata.tripPlannerAPI.service.AuthenticationService;
import com.lrasata.tripPlannerAPI.service.dto.LoginUserDTO;
import com.lrasata.tripPlannerAPI.service.dto.RegisterUserDTO;
import jakarta.servlet.http.HttpServletRequest;
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
    authenticationService.setAuthCookies(response, loginResponse);

    return ResponseEntity.ok(loginResponse);
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
    LOG.debug("REST request to refresh token");
    try {

      String refreshToken = authenticationService.extractRefreshTokenFromCookies(request);
      if (refreshToken == null || refreshToken.isEmpty()) {
        throw new RuntimeException("Missing refresh token");
      }

      LoginResponse loginResponse = authenticationService.refreshTokens(refreshToken);
      authenticationService.setAuthCookies(response, loginResponse);

      return ResponseEntity.ok(loginResponse);

    } catch (RuntimeException ex) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", ex.getMessage()));
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
    // remove refreshToken from database
    String refreshToken = authenticationService.extractRefreshTokenFromCookies(request);
    authenticationService.logout(refreshToken);

    // set token and refreshToken cookie headers to expire immediately
    ResponseCookie tokenCookie = authenticationService.setTokenCookie("", Duration.ofDays(0));
    response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());

    ResponseCookie refreshTokenCookie =
        authenticationService.setRefreshTokenCookie("", Duration.ofDays(0));
    response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

    return ResponseEntity.ok().build();
  }
}
