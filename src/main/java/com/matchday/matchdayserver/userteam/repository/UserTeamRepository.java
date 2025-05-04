package com.matchday.matchdayserver.userteam.repository;

import com.matchday.matchdayserver.userteam.model.entity.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


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

    //활동 중인 team 목록
    @Query("SELECT ut.team.id FROM UserTeam ut WHERE ut.user.id = :userId AND ut.isActive = true")
    List<Long> findActiveTeamIdsByUserId(@Param("userId") Long userId);

    @Query("""
    SELECT ut FROM UserTeam ut 
    WHERE ut.user.id = :userId AND ut.team.id = :teamId AND ut.isActive = true
    """)
    Optional<UserTeam> findActiveUserTeamByUserIdAndTeamId(@Param("userId") Long userId, @Param("teamId") Long teamId);
}
