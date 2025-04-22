package com.matchday.matchdayserver.team.repository;

import com.matchday.matchdayserver.team.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByName(String name);

    // ngram 기반 검색
    @Query(value = "SELECT * FROM team WHERE MATCH(name) AGAINST(:keyword IN NATURAL LANGUAGE MODE)", nativeQuery = true)
    List<Team> searchByKeyword(@Param("keyword") String keyword);

    @Query("""
                SELECT t FROM Team t
                WHERE (t.id = (SELECT m.homeTeam.id FROM Match m WHERE m.id = :matchId)
                OR t.id = (SELECT m.awayTeam.id FROM Match m WHERE m.id = :matchId))
                AND t.id = (SELECT ut.team.id FROM UserTeam ut WHERE ut.user.id = :userId)
            """)
    Optional<Team> findByMatchIdAndUserId(@Param("matchId") Long matchId, @Param("userId") Long userId);
}
