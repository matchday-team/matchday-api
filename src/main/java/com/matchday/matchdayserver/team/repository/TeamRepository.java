package com.matchday.matchdayserver.team.repository;

import com.matchday.matchdayserver.team.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByName(String name);
}
