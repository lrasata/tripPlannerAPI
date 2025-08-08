package com.lrasata.tripPlannerAPI.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticationServiceTest {

  @Mock UserRepository userRepository;
  @Mock RoleRepository roleRepository;
  @Mock PasswordEncoder passwordEncoder;
  @Mock AuthenticationManager authenticationManager;
  @Mock RefreshTokenRepository refreshTokenRepository;
  @Mock JwtService jwtService;
  @Mock HttpServletRequest request;
  @Mock HttpServletResponse response;

  @InjectMocks AuthenticationService authenticationService;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
    authenticationService =
        new AuthenticationService(
            userRepository,
            roleRepository,
            authenticationManager,
            passwordEncoder,
            refreshTokenRepository,
            jwtService);

    setField("accessTokenExpirationTime", 1000L);
    setField("refreshTokenExpirationTime", 2000L);
    setField("cookieSecureAttribute", true);
    setField("cookieSameSite", "Lax");
  }

  void setField(String name, Object value) throws Exception {
    Field f = AuthenticationService.class.getDeclaredField(name);
    f.setAccessible(true);
    f.set(authenticationService, value);
  }

  @Test
  void signup_userExists_returnsEmpty() {
    RegisterUserDTO dto = new RegisterUserDTO();
    dto.setEmail("test@example.com");
    when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

    Optional<User> result = authenticationService.signup(dto);

    assertThat(result).isEmpty();
  }

  @Test
  void signup_roleMissing_returnsEmpty() {
    RegisterUserDTO dto = new RegisterUserDTO();
    dto.setEmail("test@example.com");
    when(userRepository.existsByEmail(anyString())).thenReturn(false);
    when(roleRepository.findByName(RoleEnum.ROLE_PARTICIPANT)).thenReturn(Optional.empty());

    Optional<User> result = authenticationService.signup(dto);

    assertThat(result).isEmpty();
  }

  @Test
  void signup_success_returnsUser() {
    RegisterUserDTO dto = new RegisterUserDTO();
    dto.setEmail("test@example.com");
    dto.setPassword("pass");
    dto.setFullName("Test User");
    Role role = new Role();
    when(userRepository.existsByEmail(anyString())).thenReturn(false);
    when(roleRepository.findByName(RoleEnum.ROLE_PARTICIPANT)).thenReturn(Optional.of(role));
    when(passwordEncoder.encode("pass")).thenReturn("encoded");
    User savedUser = new User();
    when(userRepository.save(any(User.class))).thenReturn(savedUser);

    Optional<User> result = authenticationService.signup(dto);

    assertThat(result).contains(savedUser);
  }

  @Test
  void authenticate_success_returnsUser() {
    LoginUserDTO dto = new LoginUserDTO();
    dto.setEmail("test@example.com");
    dto.setPassword("pass");
    User user = new User();
    when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

    User result = authenticationService.authenticate(dto);

    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    assertThat(result).isEqualTo(user);
  }

  @Test
  void authenticate_userNotFound_throws() {
    LoginUserDTO dto = new LoginUserDTO();
    dto.setEmail("test@example.com");
    dto.setPassword("pass");
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> authenticationService.authenticate(dto)).isInstanceOf(Exception.class);
  }

  @Test
  void buildLoginResponse_generatesTokensAndSavesRefreshToken() {
    User user = new User();
    user.setFullName("user1");
    when(jwtService.generateRefreshToken(user)).thenReturn("refresh-jwt");
    when(jwtService.generateAccessToken(user)).thenReturn("access-jwt");
    when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArgument(0));

    LoginResponse response = authenticationService.buildLoginResponse(user);

    assertThat(response.getToken()).isEqualTo("access-jwt");
    assertThat(response.getRefreshToken()).isEqualTo("refresh-jwt");
    assertThat(response.getTokenExpiresIn()).isEqualTo(1000L);
    assertThat(response.getRefreshTokenExpiresIn()).isEqualTo(2000L);
    verify(refreshTokenRepository).save(any(RefreshToken.class));
  }

  @Test
  void login_callsAuthenticateAndBuildLoginResponse() {
    LoginUserDTO dto = new LoginUserDTO();
    dto.setEmail("test@example.com");
    dto.setPassword("password123");

    User user = new User();
    user.setEmail(dto.getEmail());

    when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
    when(jwtService.generateRefreshToken(user)).thenReturn("refresh-jwt");
    when(jwtService.generateAccessToken(user)).thenReturn("access-jwt");
    when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArgument(0));

    // Act
    LoginResponse response = authenticationService.login(dto);

    // Assert
    assertThat(response.getToken()).isEqualTo("access-jwt");
    assertThat(response.getRefreshToken()).isEqualTo("refresh-jwt");
  }

  @Test
  void refreshTokens_validToken_returnsLoginResponse() {
    RefreshToken token = new RefreshToken();
    token.setToken("refresh");
    token.setExpiryDate(Instant.now().plusSeconds(60));
    token.setUsername("user1");
    User user = new User();
    when(refreshTokenRepository.findByToken("refresh")).thenReturn(Optional.of(token));
    when(userRepository.findByEmail("user1")).thenReturn(Optional.of(user));
    when(jwtService.generateRefreshToken(any())).thenReturn("refresh-jwt");
    when(jwtService.generateAccessToken(any())).thenReturn("access-jwt");
    when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArgument(0));

    LoginResponse response = authenticationService.refreshTokens("refresh");

    assertThat(response.getToken()).isEqualTo("access-jwt");
    verify(refreshTokenRepository).deleteByToken("refresh");
  }

  @Test
  void refreshTokens_expiredToken_throwsAndDeletes() {
    RefreshToken token = new RefreshToken();
    token.setToken("refresh");
    token.setExpiryDate(Instant.now().minusSeconds(60));
    when(refreshTokenRepository.findByToken("refresh")).thenReturn(Optional.of(token));

    assertThatThrownBy(() -> authenticationService.refreshTokens("refresh"))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Expired refresh token");
    verify(refreshTokenRepository).deleteByToken("refresh");
  }

  @Test
  void refreshTokens_invalidToken_throws() {
    when(refreshTokenRepository.findByToken("refresh")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> authenticationService.refreshTokens("refresh"))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Invalid refresh token");
  }

  @Test
  void refreshTokens_userNotFound_throws() {
    RefreshToken token = new RefreshToken();
    token.setToken("refresh");
    token.setExpiryDate(Instant.now().plusSeconds(60));
    token.setUsername("user1");
    when(refreshTokenRepository.findByToken("refresh")).thenReturn(Optional.of(token));
    when(userRepository.findByEmail("user1")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> authenticationService.refreshTokens("refresh"))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("User not found");
  }

  @Test
  void createAdministrator_roleMissing_returnsNull() {
    RegisterUserDTO dto = new RegisterUserDTO();
    when(roleRepository.findByName(RoleEnum.ROLE_SUPER_ADMIN)).thenReturn(Optional.empty());

    User result = authenticationService.createAdministrator(dto);

    assertThat(result).isNull();
  }

  @Test
  void createAdministrator_success_returnsUser() {
    RegisterUserDTO dto = new RegisterUserDTO();
    dto.setEmail("admin@example.com");
    dto.setPassword("pass");
    dto.setFullName("Admin");
    Role role = new Role();
    when(roleRepository.findByName(RoleEnum.ROLE_SUPER_ADMIN)).thenReturn(Optional.of(role));
    when(passwordEncoder.encode("pass")).thenReturn("encoded");
    User savedUser = new User();
    when(userRepository.save(any(User.class))).thenReturn(savedUser);

    User result = authenticationService.createAdministrator(dto);

    assertThat(result).isEqualTo(savedUser);
  }

  @Test
  void setTokenCookie_setsCorrectProperties() {
    ResponseCookie cookie = authenticationService.setTokenCookie("token", Duration.ofSeconds(10));
    assertThat(cookie.getName()).isEqualTo("token");
    assertThat(cookie.getValue()).isEqualTo("token");
    assertThat(cookie.isHttpOnly()).isTrue();
    assertThat(cookie.isSecure()).isTrue();
    assertThat(cookie.getSameSite()).isEqualTo("Lax");
    assertThat(cookie.getMaxAge().getSeconds()).isEqualTo(10);
  }

  @Test
  void setRefreshTokenCookie_setsCorrectProperties() {
    ResponseCookie cookie =
        authenticationService.setRefreshTokenCookie("refresh", Duration.ofSeconds(20));
    assertThat(cookie.getName()).isEqualTo("refreshToken");
    assertThat(cookie.getValue()).isEqualTo("refresh");
    assertThat(cookie.isHttpOnly()).isTrue();
    assertThat(cookie.isSecure()).isTrue();
    assertThat(cookie.getSameSite()).isEqualTo("Lax");
    assertThat(cookie.getMaxAge().getSeconds()).isEqualTo(20);
  }

  @Test
  void setAuthCookies_addsHeaders() {
    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setToken("token");
    loginResponse.setTokenExpiresIn(1000L);
    loginResponse.setRefreshToken("refresh");
    loginResponse.setRefreshTokenExpiresIn(2000L);

    HttpServletResponse resp = mock(HttpServletResponse.class);
    AuthenticationService svc = authenticationService;
    svc.setAuthCookies(resp, loginResponse);

    // Just verify addHeader called twice
    verify(resp, times(2)).addHeader(eq("Set-Cookie"), anyString());
  }

  @Test
  void extractRefreshTokenFromCookies_cookiePresent_returnsValue() {
    Cookie[] cookies = {new Cookie("refreshToken", "refresh-value")};
    when(request.getCookies()).thenReturn(cookies);

    String result = authenticationService.extractRefreshTokenFromCookies(request);

    assertThat(result).isEqualTo("refresh-value");
  }

  @Test
  void extractRefreshTokenFromCookies_cookieNotPresent_returnsNull() {
    Cookie[] cookies = {new Cookie("other", "value")};
    when(request.getCookies()).thenReturn(cookies);

    String result = authenticationService.extractRefreshTokenFromCookies(request);

    assertThat(result).isNull();
  }

  @Test
  void extractRefreshTokenFromCookies_noCookies_returnsNull() {
    when(request.getCookies()).thenReturn(null);

    String result = authenticationService.extractRefreshTokenFromCookies(request);

    assertThat(result).isNull();
  }

  @Test
  void logout_deletesToken() {
    authenticationService.logout("refresh-token");
    verify(refreshTokenRepository).deleteByToken("refresh-token");
  }
}
