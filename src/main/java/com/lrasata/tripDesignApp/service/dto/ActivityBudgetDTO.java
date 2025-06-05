package com.lrasata.tripDesignApp.service.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class ActivityBudgetDTO implements Serializable {

  private Long id;

  @NotNull private Integer total;

  @NotNull private Integer budgetPerPerson;

  private Long activityId;

  public ActivityBudgetDTO() {}

  public ActivityBudgetDTO(Long id, Integer total, Integer budgetPerPerson, Long activityId) {
    this.id = id;
    this.total = total;
    this.budgetPerPerson = budgetPerPerson;
    this.activityId = activityId;
  }

  public Integer getBudgetPerPerson() {
    return budgetPerPerson;
  }

  public void setBudgetPerPerson(Integer budgetPerPerson) {
    this.budgetPerPerson = budgetPerPerson;
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getActivityId() {
    return activityId;
  }

  public void setActivityId(Long activityId) {
    this.activityId = activityId;
  }
}
