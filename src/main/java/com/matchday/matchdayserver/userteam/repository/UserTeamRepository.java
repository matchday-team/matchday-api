package com.matchday.matchdayserver.userteam.repository;

import com.matchday.matchdayserver.userteam.model.entity.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {
    boolean existsByUserIdAndTeamId(Long userId, Long teamId);
    List<UserTeam> findAllByTeamId(Long teamId);

    //활동 중인 team 목록
    @Query("SELECT ut.team.id FROM UserTeam ut WHERE ut.user.id = :userId AND ut.isActive = true")
    List<Long> findActiveTeamIdsByUserId(@Param("userId") Long userId);
}
