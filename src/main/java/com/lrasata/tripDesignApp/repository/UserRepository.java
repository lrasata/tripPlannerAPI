package com.lrasata.tripDesignApp.repository;

import com.lrasata.tripDesignApp.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByEmail(String email);

  List<User> findByEmailContaining(String emailFragment);
}
