package com.lrasata.tripPlannerAPI.service.dto;

public class LocationDTO {
  private Long id;
  private String city;
  private String region;
  private String country;
  private String countryCode;

  public LocationDTO() {}

  public LocationDTO(Long id, String city, String region, String country, String countryCode) {
    this.id = id;
    this.city = city;
    this.region = region;
    this.country = country;
    this.countryCode = countryCode;
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }
}
