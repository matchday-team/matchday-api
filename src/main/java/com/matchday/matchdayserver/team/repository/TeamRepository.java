package com.matchday.matchdayserver.team.repository;

import com.matchday.matchdayserver.team.model.dto.response.TeamListResponse;
import com.matchday.matchdayserver.team.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByName(String name);
    List<Team> findByNameContaining(String keyword); //키워드에 해당하는 팀 조회
    List<Team> findAllBy();
}
