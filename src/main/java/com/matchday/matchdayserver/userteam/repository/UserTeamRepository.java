package com.matchday.matchdayserver.userteam.repository;

import com.matchday.matchdayserver.userteam.model.entity.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {
    boolean existsByUserIdAndTeamId(Long userId, Long teamId);
    List<UserTeam> findAllByTeamId(Long teamId);
}
