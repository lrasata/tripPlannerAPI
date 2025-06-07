package com.lrasata.tripPlannerAPI.service.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class TripBudgetDTO implements Serializable {

  private Long id;

  @NotNull private Integer total;

  @NotNull private Integer budgetPerPerson;

  private Long tripId;

  public TripBudgetDTO() {}

  public TripBudgetDTO(Long id, Integer total, Integer budgetPerPerson, Long tripId) {
    this.id = id;
    this.total = total;
    this.budgetPerPerson = budgetPerPerson;
    this.tripId = tripId;
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

  public Long getTripId() {
    return tripId;
  }

  public void setTripId(Long tripId) {
    this.tripId = tripId;
  }
}
