package com.lrasata.tripDesignApp.service;

import com.lrasata.tripDesignApp.entity.Trip;
import com.lrasata.tripDesignApp.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public Optional<Trip> getTripById(Long id) {
        return tripRepository.findById(id);
    }

    public Trip saveTrip(Trip trip) {
        return tripRepository.save(trip);
    }

    public Trip updateTrip(Long id, Trip tripDetails) {
        Trip trip = tripRepository.findById(id).orElseThrow(() -> new RuntimeException("Trip not found"));
        trip.setName(tripDetails.getName());
        trip.setDescription(tripDetails.getDescription());
        trip.setBudget(tripDetails.getBudget());

        trip.setDepartureLocation(tripDetails.getDepartureLocation());
        trip.setDepartureDate(tripDetails.getDepartureDate());

        trip.setArrivalLocation(tripDetails.getArrivalLocation());
        trip.setReturnDate(tripDetails.getReturnDate());

        return tripRepository.save(trip);
    }

    public void deleteTrip(Long id) {
        tripRepository.deleteById(id);
    }
}
