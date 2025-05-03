package com.matchday.matchdayserver.matchevent.repository;

import com.matchday.matchdayserver.matchevent.model.dto.EventTypeCount;
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

    List<MatchEvent> findByMatchId(Long matchId);
           
    void deleteByMatchId(Long matchId);

    @Query(value = """
    SELECT 
      event_type AS eventType,
      COUNT(*) AS count
    FROM match_event
    WHERE match_user_id = :matchUserId
      AND match_id = :matchId
    GROUP BY event_type
    """, nativeQuery = true)
    List<EventTypeCount> countEventTypesByMatchUserAndMatch(
        @Param("matchUserId") Long matchUserId,
        @Param("matchId") Long matchId
    );
}