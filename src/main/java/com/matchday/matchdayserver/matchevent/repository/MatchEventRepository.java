package com.matchday.matchdayserver.matchevent.repository;

import com.matchday.matchdayserver.matchevent.model.dto.EventTypeCount;
import org.springframework.data.jpa.repository.JpaRepository;

import com.matchday.matchdayserver.matchevent.model.entity.MatchEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MatchEventRepository extends JpaRepository<MatchEvent, Long> {
  @Query(value = """
    SELECT 
      event_type AS eventType,
      COUNT(*) AS count
    FROM match_event
    WHERE match_id = :matchId AND user_id = :userId
    GROUP BY event_type
    """, nativeQuery = true)
  List<EventTypeCount> countEventTypesByUserAndMatch(@Param("matchId") Long matchId, @Param("userId") Long userId);
}