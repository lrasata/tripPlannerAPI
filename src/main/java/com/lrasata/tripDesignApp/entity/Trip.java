package com.lrasata.tripDesignApp.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String departureLocation;
    private LocalDate departureDate;
    private String arrivalLocation;
    private LocalDate returnDate;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "budget_id", referencedColumnName = "id")
//    private Budget budget;
//
//    // One trip has many activities
//    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Activity> activities = new ArrayList<>();

//    @ManyToMany(mappedBy = "trips")
//    private List<User> participants = new ArrayList<>();

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

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalLocation() {
        return arrivalLocation;
    }

    public void setArrivalLocation(String arrivalLocation) {
        this.arrivalLocation = arrivalLocation;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

//    public Budget getBudget() {
//        return budget;
//    }
//
//    public void setBudget(Budget budget) {
//        this.budget = budget;
//    }
//
//    public List<Activity> getActivities() {
//        return activities;
//    }
//
//    public void setActivities(List<Activity> activities) {
//        this.activities = activities;
//    }
//
//    public void addActivity(Activity activity) {
//        activities.add(activity);
//        activity.setTrip(this);
//    }
//
//    public void removeActivity(Activity activity) {
//        activities.remove(activity);
//        activity.setTrip(null);
//    }

//    public List<User> getParticipants() {
//        return participants;
//    }
//
//    public void setParticipants(List<User> participants) {
//        this.participants = participants;
//    }
//
//    public void addParticipant(User user) {
//        participants.add(user);
//        user.getTrips().add(this);
//    }
//
//    public void removeParticipant(User user) {
//        participants.remove(user);
//        user.getTrips().remove(this);
//    }
}
