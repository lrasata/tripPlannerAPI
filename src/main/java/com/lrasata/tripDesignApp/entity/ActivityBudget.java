package com.lrasata.tripDesignApp.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ActivityBudget")
public class ActivityBudget extends Budget {
}
