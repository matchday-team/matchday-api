package com.matchday.matchdayserver.user.repository;

import com.matchday.matchdayserver.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsById(Long id);

    /**
     * 팀에 가장 많이 출전한 선수 이름 조회
     * @param teamId 팀 ID
     * @return 가장 많이 출전한 선수 이름
     */
    @Query(value = """
        SELECT u.name 
        FROM user u
        JOIN match_user mu ON u.id = mu.user_id
        JOIN `match` m ON mu.match_id = m.id
        WHERE mu.team_id = :teamId 
        GROUP BY u.id
        ORDER BY COUNT(mu.id) DESC
        LIMIT 1
        """, nativeQuery = true)
    String findMostPlayedPlayerName(@Param("teamId") Long teamId);
    User findBySocialId(String socialId);
}
