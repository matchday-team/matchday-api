package com.matchday.matchdayserver.userteam.repository;

import com.matchday.matchdayserver.userteam.model.entity.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {
    boolean existsByUserIdAndTeamId(Long userId, Long teamId);
    List<UserTeam> findAllByTeamId(Long teamId);

    @Query("""
    SELECT ut FROM UserTeam ut
    JOIN Match m ON m.homeTeam.id = ut.team.id
    WHERE ut.user.id = :userId AND m.id = :matchId AND ut.isActive = true
    """)
    Optional<UserTeam> findActiveUserInHomeTeam(Long userId, Long matchId);

}
