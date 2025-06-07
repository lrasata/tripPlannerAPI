package com.lrasata.tripPlannerAPI.service.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class TripDTO implements Serializable {
  private Long id;

  @NotNull private String name;
  private String description;
  private LocalDate departureDate;
  private LocalDate returnDate;
  private LocationDTO departureLocation;
  private LocationDTO arrivalLocation;
  private Integer participantCount;
  private List<Long> participantIds;

  //    private TripBudgetDTO budget;
  //    private List<ActivityDTO> activities;

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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDate getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(LocalDate departureDate) {
    this.departureDate = departureDate;
  }

  public LocalDate getReturnDate() {
    return returnDate;
  }

  public void setReturnDate(LocalDate returnDate) {
    this.returnDate = returnDate;
  }

  public LocationDTO getDepartureLocation() {
    return departureLocation;
  }

  public void setDepartureLocation(LocationDTO departureLocation) {
    this.departureLocation = departureLocation;
  }

  public LocationDTO getArrivalLocation() {
    return arrivalLocation;
  }

  public void setArrivalLocation(LocationDTO arrivalLocation) {
    this.arrivalLocation = arrivalLocation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TripDTO tripDTO = (TripDTO) o;
    return Objects.equals(id, tripDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public List<Long> getParticipantIds() {
    return participantIds;
  }

  public void setParticipantIds(List<Long> participantIds) {
    this.participantIds = participantIds;
  }

  public Integer getParticipantCount() {
    return participantCount;
  }

  public void setParticipantCount(Integer participantCount) {
    this.participantCount = participantCount;
  }
}
