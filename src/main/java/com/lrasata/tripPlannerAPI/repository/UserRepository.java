package com.lrasata.tripPlannerAPI.repository;

import com.lrasata.tripPlannerAPI.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByEmail(String email);

  List<User> findByEmailContaining(String emailFragment);

  Optional<User> findByEmail(String email);
}
