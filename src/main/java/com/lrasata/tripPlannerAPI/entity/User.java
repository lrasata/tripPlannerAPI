package com.lrasata.tripPlannerAPI.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "app_user")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @NotNull
  @Column(nullable = false)
  private String fullName;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  private Role role;

  @CreationTimestamp
  @Column(updatable = false, name = "created_at")
  private Date createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private Date updatedAt;

  @ManyToMany
  @JoinTable(
      name = "user_trip",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "trip_id"))
  private List<Trip> trips = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String name) {
    this.fullName = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<Trip> getTrips() {
    return trips;
  }

  public void setTrips(List<Trip> trips) {
    this.trips = trips;
  }

  public void addTrip(Trip trip) {
    trips.add(trip);
    trip.getParticipants().add(this);
  }

  public void removeTrip(Trip trip) {
    trips.remove(trip);
    trip.getParticipants().remove(this);
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    // return UserDetails.super.isAccountNonExpired();
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    // return UserDetails.super.isAccountNonLocked();
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    // return UserDetails.super.isCredentialsNonExpired();
    return true;
  }

  @Override
  public boolean isEnabled() {
    // return UserDetails.super.isEnabled();
    return true;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }
}
