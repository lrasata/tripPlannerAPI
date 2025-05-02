package com.lrasata.tripDesignApp.service.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class TripDTO implements Serializable {
    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private String departureLocation;

    @NotNull
    private Date departureDate;

    @NotNull
    private String arrivalLocation;

    @NotNull
    private Date returnDate;

    private TripBudgetDTO budget;
    private List<ActivityDTO> activities;
    private List<Long> participantIds;

    // Constructors
    public TripDTO() {
    }

    public TripDTO(Long id, String name, String description, String departureLocation, Date departureDate,
                   String arrivalLocation, Date returnDate, TripBudgetDTO budget,
                   List<ActivityDTO> activities, List<Long> participantIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.departureLocation = departureLocation;
        this.departureDate = departureDate;
        this.arrivalLocation = arrivalLocation;
        this.returnDate = returnDate;
        this.budget = budget;
        this.activities = activities;
        this.participantIds = participantIds;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalLocation() {
        return arrivalLocation;
    }

    public void setArrivalLocation(String arrivalLocation) {
        this.arrivalLocation = arrivalLocation;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public TripBudgetDTO getBudget() {
        return budget;
    }

    public void setBudget(TripBudgetDTO budget) {
        this.budget = budget;
    }

    public List<ActivityDTO> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityDTO> activities) {
        this.activities = activities;
    }

    public List<Long> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<Long> participantIds) {
        this.participantIds = participantIds;
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
}
