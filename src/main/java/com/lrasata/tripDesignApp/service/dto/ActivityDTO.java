package com.lrasata.tripDesignApp.service.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SuppressWarnings("common-java:DuplicatedBlocks")
public class ActivityDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private String location;

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;

    private List<String> urls;

    private Long tripId;

    private ActivityBudgetDTO budget;

    // Constructors
    public ActivityDTO() {}

    public ActivityDTO(Long id, String name, String description, String location,
                       Date startDate, Date endDate, List<String> urls,
                       Long tripId, ActivityBudgetDTO budget) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.urls = urls;
        this.tripId = tripId;
        this.budget = budget;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public ActivityBudgetDTO getBudget() {
        return budget;
    }

    public void setBudget(ActivityBudgetDTO budget) {
        this.budget = budget;
    }
}
