package com.adas.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.adas.authservice.model.ERole;
import com.adas.authservice.model.Role;
import com.adas.authservice.repository.RoleRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Optional<Role> findByName(ERole name) {
        return roleRepository.findByName(name);
    }

}
