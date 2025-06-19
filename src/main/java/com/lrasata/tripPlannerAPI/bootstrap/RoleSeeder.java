package com.lrasata.tripPlannerAPI.bootstrap;

import com.lrasata.tripPlannerAPI.entity.Role;
import com.lrasata.tripPlannerAPI.entity.RoleEnum;
import com.lrasata.tripPlannerAPI.repository.RoleRepository;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {
  private static final Logger LOG = LoggerFactory.getLogger(RoleSeeder.class);
  private final RoleRepository roleRepository;

  public RoleSeeder(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    this.loadRoles();
  }

  private void loadRoles() {
    RoleEnum[] roleNames =
        new RoleEnum[] {RoleEnum.ROLE_PARTICIPANT, RoleEnum.ROLE_ADMIN, RoleEnum.ROLE_SUPER_ADMIN};
    Map<RoleEnum, String> roleDescriptionMap =
        Map.of(
            RoleEnum.ROLE_PARTICIPANT, "Default user role",
            RoleEnum.ROLE_ADMIN, "Administrator role",
            RoleEnum.ROLE_SUPER_ADMIN, "Super Administrator role");

    Arrays.stream(roleNames)
        .forEach(
            (roleName) -> {
              Optional<Role> optionalRole = roleRepository.findByName(roleName);

              optionalRole.ifPresentOrElse(
                  role -> LOG.info("Role '{}' already exists. Skipping creation.", role.getName()),
                  () -> {
                    Role roleToCreate = new Role();

                    roleToCreate.setName(roleName);
                    roleToCreate.setDescription(roleDescriptionMap.get(roleName));

                      try {
                          roleRepository.save(roleToCreate);
                          LOG.info("Created new role '{}'", roleName);
                      } catch (Exception e) {
                          LOG.error("Failed to create role '{}': {}", roleName, e.getMessage());
                      }
                  });
            });
  }
}
