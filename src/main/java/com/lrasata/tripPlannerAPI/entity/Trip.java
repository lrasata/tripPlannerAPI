package com.lrasata.tripPlannerAPI.entity;

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
  private LocalDate departureDate;
  private LocalDate returnDate;
  private Integer participantCount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "departure_location_id")
  private Location departureLocation;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "arrival_location_id")
  private Location arrivalLocation;

  //    @OneToOne(cascade = CascadeType.ALL)
  //    @JoinColumn(name = "budget_id", referencedColumnName = "id")
  //    private Budget budget;
  //
  //    // One trip has many activities

  //    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
  //    private List<Activity> activities = new ArrayList<>();

  @ManyToMany(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "trip_users",
      joinColumns = @JoinColumn(name = "trip_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id"))
  private List<User> participants = new ArrayList<>();

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

  public Location getDepartureLocation() {
    return departureLocation;
  }

  public void setDepartureLocation(Location location) {
    this.departureLocation = location;
  }

  public Location getArrivalLocation() {
    return arrivalLocation;
  }

  public void setArrivalLocation(Location arrivalLocation) {
    this.arrivalLocation = arrivalLocation;
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

  public List<User> getParticipants() {
    return participants;
  }

  public void setParticipants(List<User> participants) {
    this.participants
        .clear(); // IMPORTANT: Clear existing participants to handle removals correctly
    if (participants != null) {
      this.participants.addAll(participants);
    }
  }

  public void addParticipant(User user) {
    participants.add(user);
    user.getTrips().add(this);
  }

  public void removeParticipant(User user) {
    participants.remove(user);
    user.getTrips().remove(this);
  }

  public Integer getParticipantCount() {
    return participantCount;
  }

  public void setParticipantCount(Integer participantCount) {
    this.participantCount = participantCount;
  }
}
