package com.lrasata.tripDesignApp.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TripBudget")
public class TripBudget extends Budget {}
