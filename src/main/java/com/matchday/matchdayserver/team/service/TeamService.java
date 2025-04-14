package com.matchday.matchdayserver.team.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.TeamStatus;
import com.matchday.matchdayserver.team.model.dto.request.TeamCreateRequest;
import com.matchday.matchdayserver.team.model.dto.response.TeamBasicDto;
import com.matchday.matchdayserver.team.model.dto.response.TeamListResponse;
import com.matchday.matchdayserver.team.model.entity.Team;
import com.matchday.matchdayserver.team.repository.TeamRepository;
import com.matchday.matchdayserver.userteam.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;

    public void create(TeamCreateRequest request){
        validateDuplicateTeamName(request.getName());
        Team team = new Team(request.getName());
        teamRepository.save(team);
    }

    //팀 이름 중복 체크
    public void validateDuplicateTeamName(String name) {
        if (teamRepository.existsByName(name)) {
            throw new ApiException(TeamStatus.DUPLICATE_TEAMNAME);
        }
    }

    //팀에 특정 유저가 존재하는지 체크
    public boolean validateUserInTeam(Long userId, Long teamId){
        return userTeamRepository.existsByUserIdAndTeamId(userId, teamId);
    }

    //키워드에 해당하는 팀 검색
    public List<TeamBasicDto> searchTeams(String keyword) {
        List<Team> teams = teamRepository.findByNameContaining(keyword);

        if (teams.isEmpty()) {
            throw new ApiException(TeamStatus.NOTFOUND_TEAM);
        }

        return teams.stream()
                .map(team -> new TeamBasicDto(team.getId(), team.getName()))
                .collect(Collectors.toList());
    }
}
