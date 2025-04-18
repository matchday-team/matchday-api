package com.matchday.matchdayserver.team.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.TeamStatus;
import com.matchday.matchdayserver.team.model.dto.request.TeamCreateRequest;
import com.matchday.matchdayserver.team.model.dto.response.TeamMemberListResponse;
import com.matchday.matchdayserver.team.model.dto.response.TeamMemberResponse;
import com.matchday.matchdayserver.team.model.dto.response.TeamNameResponse;
import com.matchday.matchdayserver.team.model.entity.Team;
import com.matchday.matchdayserver.team.repository.TeamRepository;
import com.matchday.matchdayserver.userteam.model.entity.UserTeam;
import com.matchday.matchdayserver.userteam.model.mapper.UserTeamMapper;
import com.matchday.matchdayserver.userteam.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;

    //팀 생성
    public Long create(TeamCreateRequest request){
        validateDuplicateTeamName(request.getName());
        Team team = new Team(request.getName());
        teamRepository.save(team);
        return team.getId();
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
    public List<TeamNameResponse> searchTeams(String keyword) {
        List<Team> teams = teamRepository.searchByKeyword(keyword);

        if (teams.isEmpty()) {
            throw new ApiException(TeamStatus.NOTFOUND_TEAM);
        }

        return teams.stream()
                .map(team -> new TeamNameResponse(team.getId(), team.getName()))
                .collect(Collectors.toList());
    }

    //전체 팀 조회
    public List<TeamNameResponse> getAllTeams(){
        List<Team> teams = teamRepository.findAllBy();

        return teams.stream()
                .map(team -> new TeamNameResponse(team.getId(), team.getName()))
                .collect(Collectors.toList());
    }

    //팀에 속한 선수들 조회
    public TeamMemberListResponse getTeamMembers(Long teamId){
        List<UserTeam> userTeams = userTeamRepository.findAllByTeamId(teamId);
        List<TeamMemberResponse> userTeamMembers = userTeams.stream()
                .map(userTeam -> UserTeamMapper.toTeamMemberResponse(userTeam))
                .collect(Collectors.toList());
        return new TeamMemberListResponse(userTeamMembers);
    }
}
