package com.matchday.matchdayserver.auth.repository;

import com.matchday.matchdayserver.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}