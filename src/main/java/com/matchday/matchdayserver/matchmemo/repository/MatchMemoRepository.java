package com.matchday.matchdayserver.matchmemo.repository;

import com.matchday.matchdayserver.matchmemo.model.entity.MatchMemo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchMemoRepository extends JpaRepository<MatchMemo, Long> {
  Optional<MatchMemo> findByMatchIdAndTeamId(Long matchId, Long teamId);
  void deleteByMatchIdAndTeamId(Long matchId, Long teamId);
}