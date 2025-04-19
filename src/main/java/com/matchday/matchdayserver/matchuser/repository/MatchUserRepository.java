package com.matchday.matchdayserver.matchuser.repository;

import com.matchday.matchdayserver.matchuser.model.entity.MatchUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchUserRepository extends JpaRepository<MatchUser, Long> {
}
