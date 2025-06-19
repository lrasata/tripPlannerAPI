package com.lrasata.tripPlannerAPI.service.dto;

import java.io.Serializable;

public class RoleDTO implements Serializable {

  private String name;

  public RoleDTO() {}

  public RoleDTO(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
