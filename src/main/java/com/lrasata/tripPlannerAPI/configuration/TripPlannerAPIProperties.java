package com.lrasata.tripPlannerAPI.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "trip-design-app")
public class TripPlannerAPIProperties {
  private String allowedOrigin;

  public String getAllowedOrigin() {
    return allowedOrigin;
  }

  public void setAllowedOrigin(String allowedOrigin) {
    this.allowedOrigin = allowedOrigin;
  }
}
