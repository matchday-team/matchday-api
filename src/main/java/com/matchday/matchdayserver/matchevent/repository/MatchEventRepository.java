package com.matchday.matchdayserver.matchevent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matchday.matchdayserver.matchevent.model.entity.MatchEvent;

public interface MatchEventRepository extends JpaRepository<MatchEvent, Long> {
  void deleteByMatchId(Long matchId);
}