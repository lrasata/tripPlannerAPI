package com.lrasata.tripDesignApp.service;

import com.lrasata.tripDesignApp.entity.Trip;
import com.lrasata.tripDesignApp.repository.TripRepository;
import com.lrasata.tripDesignApp.service.dto.TripDTO;
import com.lrasata.tripDesignApp.service.mapper.TripMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TripService {

    private static final Logger LOG = LoggerFactory.getLogger(TripService.class);

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TripMapper tripMapper;

    public List<TripDTO> findAll() {
        LOG.debug("Request to get all trips");
        return tripRepository.findAll().stream().map(tripMapper::toDto).collect(Collectors.toList());
    }

    public List<TripDTO> findTripsInPast() {
        return tripRepository.findByDepartureDateBefore(LocalDate.now())
                .stream()
                .map(tripMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TripDTO> findTripsInFuture() {
        return tripRepository.findByDepartureDateAfter(LocalDate.now())
                .stream()
                .map(tripMapper::toDto)
                .collect(Collectors.toList());
    }


    public Optional<TripDTO> findOneById(Long id) {
        LOG.debug("Request to get Trip : {}", id);
        return tripRepository.findById(id).map(tripMapper::toDto);
    }

    public TripDTO saveTrip(TripDTO tripDTO) {
        LOG.debug("Request to save Trip : {}", tripDTO);
        Trip entityTrip = tripMapper.toEntity(tripDTO);
        Trip savedTrip = tripRepository.save(entityTrip);
        return tripMapper.toDto(savedTrip);
    }

    public TripDTO updateTrip(Long id, TripDTO tripDTO) {
        LOG.debug("Request to update Trip : {}", tripDTO);
        tripRepository.findById(id).orElseThrow(() -> new RuntimeException("Trip not found"));

        Trip trip = tripMapper.toEntity(tripDTO);
        trip.setId(id);
        Trip savedTrip = tripRepository.save(trip);
        return tripMapper.toDto(savedTrip);
    }

    public void deleteTrip(Long id) {
        tripRepository.deleteById(id);
    }
}
