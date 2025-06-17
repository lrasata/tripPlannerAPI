package com.lrasata.tripPlannerAPI.repository;

import com.lrasata.tripPlannerAPI.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);

  void deleteByToken(String token);

  void deleteByUsername(String username);
}
