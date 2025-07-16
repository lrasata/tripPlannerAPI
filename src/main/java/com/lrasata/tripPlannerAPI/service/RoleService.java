package com.lrasata.tripPlannerAPI.service;

import com.lrasata.tripPlannerAPI.repository.RoleRepository;
import com.lrasata.tripPlannerAPI.service.dto.RoleDTO;
import com.lrasata.tripPlannerAPI.service.mapper.RoleMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

  private final RoleRepository roleRepository;
  private final RoleMapper roleMapper;

  public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
    this.roleRepository = roleRepository;
    this.roleMapper = roleMapper;
  }

  public List<RoleDTO> findAll() {
    return roleRepository.findAll().stream().map(roleMapper::toDto).collect(Collectors.toList());
  }
}
