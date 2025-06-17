package com.lrasata.tripPlannerAPI.bootstrap;

import com.lrasata.tripPlannerAPI.entity.Role;
import com.lrasata.tripPlannerAPI.entity.RoleEnum;
import com.lrasata.tripPlannerAPI.entity.User;
import com.lrasata.tripPlannerAPI.repository.RoleRepository;
import com.lrasata.tripPlannerAPI.repository.UserRepository;
import com.lrasata.tripPlannerAPI.service.dto.RegisterUserDTO;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {
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
    RegisterUserDTO userDto = new RegisterUserDTO();
    userDto.setFullName(superAdminFullName);
    userDto.setEmail(superAdminEmail);
    userDto.setPassword(superAdminPassword);

    Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ROLE_SUPER_ADMIN);
    Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());

    if (optionalRole.isEmpty() || optionalUser.isPresent()) {
      return;
    }

    User user = new User();
    user.setFullName(userDto.getFullName());
    user.setEmail(userDto.getEmail());
    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    user.setRole(optionalRole.get());

    userRepository.save(user);
  }
}
