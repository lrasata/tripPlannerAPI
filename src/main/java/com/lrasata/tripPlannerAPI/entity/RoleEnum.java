package com.lrasata.tripPlannerAPI.entity;

public enum RoleEnum {
  ROLE_SUPER_ADMIN("Super Admin"),
  ROLE_ADMIN("Admin"),
  ROLE_PARTICIPANT("Participant");

  private final String label;

  RoleEnum(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
