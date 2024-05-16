package com.adas.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adas.authservice.model.ERole;
import com.adas.authservice.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(ERole name);
}
