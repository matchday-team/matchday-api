package com.matchday.matchdayserver.match.repository;

import com.matchday.matchdayserver.match.model.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
