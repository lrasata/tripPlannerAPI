package com.lrasata.tripPlannerAPI.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ActivityBudget")
public class ActivityBudget extends Budget {}
