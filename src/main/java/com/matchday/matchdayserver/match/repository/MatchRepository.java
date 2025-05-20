package com.matchday.matchdayserver.match.repository;

import com.matchday.matchdayserver.match.model.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByHomeTeamIdOrAwayTeamId(Long homeTeamId, Long awayTeamId);

    @Query(value = "SELECT * FROM `match` ORDER BY id LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Match> findMatchesByOffsetAndLimit(@Param("offset") int offset, @Param("limit") int limit);

    List<Match> findAllByHomeTeamIdOrAwayTeamId(Long homeTeamId, Long awayTeamId);

    List<Match> findDistinctByHomeTeamIdOrAwayTeamIdOrderByMatchDateDesc(Long homeTeamId, Long awayTeamId);
}
