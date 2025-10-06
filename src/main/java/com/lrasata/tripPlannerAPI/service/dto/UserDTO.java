package com.lrasata.tripPlannerAPI.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

public class UserDTO implements Serializable {

  private Long id;

  @NotNull
  @Size(
      min = 2,
      max = 100,
      message = "The length of full name must be between 2 and 100 characters.")
  private String fullName;

  @NotNull
  @Email(
      message = "The email address is invalid.",
      flags = {Pattern.Flag.CASE_INSENSITIVE})
  private String email;

  private RoleDTO role;

  private List<Long> tripIds;

  public UserDTO() {}

  public UserDTO(Long id, String fullName, String email, RoleDTO role, List<Long> tripIds) {
    this.id = id;
    this.fullName = fullName;
    this.email = email;
    this.role = role;
    this.tripIds = tripIds;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<Long> getTripIds() {
    return tripIds;
  }

  public void setTripIds(List<Long> tripIds) {
    this.tripIds = tripIds;
  }

  public RoleDTO getRole() {
    return role;
  }

  public void setRole(RoleDTO role) {
    this.role = role;
  }
}
