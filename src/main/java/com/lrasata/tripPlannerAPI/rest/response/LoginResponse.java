package com.lrasata.tripPlannerAPI.rest.response;

public class LoginResponse {
  private String token;

  private String refreshToken;

  private long tokenExpiresIn;

  private long refreshTokenExpiresIn;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public long getTokenExpiresIn() {
    return tokenExpiresIn;
  }

  public void setTokenExpiresIn(long tokenExpiresIn) {
    this.tokenExpiresIn = tokenExpiresIn;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public long getRefreshTokenExpiresIn() {
    return refreshTokenExpiresIn;
  }

  public void setRefreshTokenExpiresIn(long refreshTokenExpiresIn) {
    this.refreshTokenExpiresIn = refreshTokenExpiresIn;
  }
}
