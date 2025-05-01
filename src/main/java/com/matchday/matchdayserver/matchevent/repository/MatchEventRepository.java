package com.matchday.matchdayserver.matchevent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matchday.matchdayserver.matchevent.model.entity.MatchEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MatchEventRepository extends JpaRepository<MatchEvent, Long> {

  @Query("""
          select me from MatchEvent me
          join fetch me.matchUser as mu
          join fetch me.match as m
          where m.id = :id
      """)
  List<MatchEvent> findByMatchIdFetchMatchUserAndMatch(@Param("id") Long matchId);
}