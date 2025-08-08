package com.lrasata.tripPlannerAPI.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.rest.response.LoginResponse;
import com.lrasata.tripPlannerAPI.service.AuthenticationService;
import com.lrasata.tripPlannerAPI.service.dto.LoginUserDTO;
import com.lrasata.tripPlannerAPI.service.dto.RegisterUserDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

public class AuthenticationControllerTest {

  @Mock private AuthenticationService authenticationService;

  @InjectMocks private AuthenticationController authenticationController;

  @Mock private HttpServletRequest request;

  @Mock private HttpServletResponse response;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testRegister_UserCreated() {
    RegisterUserDTO dto = new RegisterUserDTO();
    dto.setEmail("test@example.com");

    User user = new User();
    user.setEmail("test@example.com");

    when(authenticationService.signup(dto)).thenReturn(Optional.of(user));

    ResponseEntity<?> result = authenticationController.register(dto);

    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isEqualTo(user);
  }

  @Test
  void testRegister_UserNotCreated() {
    RegisterUserDTO dto = new RegisterUserDTO();
    dto.setEmail("test@example.com");

    when(authenticationService.signup(dto)).thenReturn(Optional.empty());

    ResponseEntity<?> result = authenticationController.register(dto);

    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(result.getBody()).isEqualTo("User could not be created");
  }

  @Test
  void testAuthenticate_Success() {
    LoginUserDTO dto = new LoginUserDTO();
    dto.setEmail("test@example.com");

    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setToken("jwt-token");
    loginResponse.setRefreshToken("refresh-token");

    when(authenticationService.login(dto)).thenReturn(loginResponse);

    ResponseEntity<LoginResponse> result = authenticationController.authenticate(dto, response);

    verify(authenticationService).setAuthCookies(response, loginResponse);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).isEqualTo(loginResponse);
  }

  @Test
  void testRefreshToken_Success() {
    String refreshToken = "refresh-token";
    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setToken("new-jwt-token");
    loginResponse.setRefreshToken("new-refresh-token");

    when(authenticationService.extractRefreshTokenFromCookies(request)).thenReturn(refreshToken);
    when(authenticationService.refreshTokens(refreshToken)).thenReturn(loginResponse);

    ResponseEntity<?> responseEntity = authenticationController.refresh(request, response);

    verify(authenticationService).setAuthCookies(response, loginResponse);
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody()).isEqualTo(loginResponse);
  }

  @Test
  void testRefreshToken_MissingRefreshToken() {
    when(authenticationService.extractRefreshTokenFromCookies(request)).thenReturn(null);

    ResponseEntity<?> responseEntity = authenticationController.refresh(request, response);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    assertThat(((Map<?, ?>) responseEntity.getBody()).get("error"))
        .isEqualTo("Missing refresh token");
  }

  @Test
  void testLogout() {
    Cookie refreshCookie = new Cookie("refreshToken", "refresh-token");
    when(authenticationService.extractRefreshTokenFromCookies(request)).thenReturn("refresh-token");

    // Mock token and refresh token cookies
    ResponseCookie tokenCookie = ResponseCookie.from("token", "").maxAge(0).build();
    ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "").maxAge(0).build();

    when(authenticationService.setTokenCookie(anyString(), any(Duration.class)))
        .thenReturn(tokenCookie);
    when(authenticationService.setRefreshTokenCookie(anyString(), any(Duration.class)))
        .thenReturn(refreshTokenCookie);

    ResponseEntity<Void> responseEntity = authenticationController.logout(request, response);

    verify(authenticationService).logout("refresh-token");
    verify(response).addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
    verify(response).addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
