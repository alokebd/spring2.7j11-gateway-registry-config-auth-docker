package com.adas.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adas.authservice.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByToken(String token);

}
