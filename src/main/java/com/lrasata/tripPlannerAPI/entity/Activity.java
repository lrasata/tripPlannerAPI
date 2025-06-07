package com.lrasata.tripPlannerAPI.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Activity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String description;
  private String location;
  private Date startDate;
  private Date endDate;

  @ElementCollection
  @CollectionTable(name = "urls", joinColumns = @JoinColumn(name = "entity_id"))
  @Column(name = "url")
  private List<String> urls = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "trip_id", nullable = false)
  private Trip trip;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "budget_id", referencedColumnName = "id")
  private Budget budget;

  // Getters and setters

  public Trip getTrip() {
    return trip;
  }

  public void setTrip(Trip trip) {
    this.trip = trip;
  }
}
