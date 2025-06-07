package com.lrasata.tripPlannerAPI.service.dto;

import com.lrasata.tripPlannerAPI.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

public class UserDTO implements Serializable {

  private Long id;

  @NotNull
  @Size(max = 50)
  private String name;

  @NotNull
  @Email
  @Size(min = 5, max = 254)
  private String email;

  private Role
      role; // TODO this should be a Valid Role from enum list - introduce custom @ValidRole

  private List<Long> tripIds;

  public UserDTO() {}

  public UserDTO(Long id, String name, String email, Role role, List<Long> tripIds) {
    this.id = id;
    this.name = name;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public List<Long> getTripIds() {
    return tripIds;
  }

  public void setTripIds(List<Long> tripIds) {
    this.tripIds = tripIds;
  }
}
