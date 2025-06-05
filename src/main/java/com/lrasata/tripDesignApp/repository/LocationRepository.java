package com.lrasata.tripDesignApp.repository;

import com.lrasata.tripDesignApp.entity.Location;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
  Optional<Location> findByCityAndCountryCode(String city, String countryCode);
}
