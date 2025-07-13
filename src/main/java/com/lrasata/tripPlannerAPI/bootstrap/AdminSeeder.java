package com.lrasata.tripPlannerAPI.bootstrap;

import com.lrasata.tripPlannerAPI.entity.Role;
import com.lrasata.tripPlannerAPI.entity.RoleEnum;
import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.repository.RoleRepository;
import com.lrasata.tripPlannerAPI.repository.UserRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {
  private static final Logger LOG = LoggerFactory.getLogger(AdminSeeder.class);
  private final RoleRepository roleRepository;

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  @Value("${trip-design-app.super-admin.fullname}")
  private String superAdminFullName;

  @Value("${trip-design-app.super-admin.email}")
  private String superAdminEmail;

  @Value("${trip-design-app.super-admin.password}")
  private String superAdminPassword;

  public AdminSeeder(
      RoleRepository roleRepository,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder) {
    this.roleRepository = roleRepository;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    this.createSuperAdministrator();
  }

  private void createSuperAdministrator() {
    Role superAdminRole =
        roleRepository
            .findByName(RoleEnum.ROLE_SUPER_ADMIN)
            .orElseGet(
                () ->
                    roleRepository.save(
                        new Role(RoleEnum.ROLE_SUPER_ADMIN, "Super Administrator role")));

    Optional<User> optionalUser = userRepository.findByEmail(superAdminEmail);

    if (optionalUser.isPresent()) {
      // Update password to keep it fresh (useful in CI)
      User existing = optionalUser.get();
      existing.setPassword(passwordEncoder.encode(superAdminPassword));
      userRepository.save(existing);
      LOG.info("Super admin user already existed, password updated: {}", superAdminEmail);
    } else {
      User user = new User();
      user.setFullName(superAdminFullName);
      user.setEmail(superAdminEmail);
      user.setPassword(passwordEncoder.encode(superAdminPassword));
      user.setRole(superAdminRole);

      userRepository.save(user);
      LOG.info("Super admin user created : {}", superAdminEmail);
    }
  }
}
