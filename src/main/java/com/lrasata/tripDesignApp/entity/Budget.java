package com.lrasata.tripDesignApp.entity;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "budget_type", discriminatorType = DiscriminatorType.STRING)
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer total;
    private Integer budgetPerPerson;

//    @OneToOne(mappedBy = "budget")
//    private Trip trip;

    @OneToOne(mappedBy = "budget")
    private Activity activity;

    public Long getId() {
        return id;
    }

    public Integer getBudgetPerPerson() {
        return budgetPerPerson;
    }

    public void setBudgetPerPerson(Integer budgetPerPerson) {
        this.budgetPerPerson = budgetPerPerson;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

//    public Trip getTrip() {
//        return trip;
//    }
//
//    public void setTrip(Trip trip) {
//        this.trip = trip;
//    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
