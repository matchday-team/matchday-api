package com.matchday.matchdayserver.team.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.TeamStatus;
import com.matchday.matchdayserver.matchevent.model.enums.MatchEventType;
import com.matchday.matchdayserver.matchevent.repository.MatchEventRepository;
import com.matchday.matchdayserver.matchuser.repository.MatchUserRepository;
import com.matchday.matchdayserver.team.model.dto.request.TeamCreateRequest;
import com.matchday.matchdayserver.team.model.dto.response.*;
import com.matchday.matchdayserver.team.model.entity.Team;
import com.matchday.matchdayserver.team.model.mapper.TeamMapper;
import com.matchday.matchdayserver.team.repository.TeamRepository;
import com.matchday.matchdayserver.user.model.dto.request.UserJoinTeamRequest;
import com.matchday.matchdayserver.user.service.UserService;
import com.matchday.matchdayserver.userteam.model.entity.UserTeam;
import com.matchday.matchdayserver.userteam.model.mapper.UserTeamMapper;
import com.matchday.matchdayserver.userteam.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;
    private final MatchUserRepository matchUserRepository;
    private final MatchEventRepository matchEventRepository;
    @Lazy
    private final UserService userService;

    //팀 생성
    public Long create(TeamCreateRequest request,Long userId){
        validateDuplicateTeamName(request.getName());
        //새로운 팀 생성
        Team team = new Team(request.getName(), request.getTeamColor(), request.getBottomColor(), request.getStockingColor(),
            request.getTeamImg());
        teamRepository.save(team);
        //팀 생성자는 팀과 연관관계를 맺음
        UserJoinTeamRequest userJoinTeamRequest = UserJoinTeamRequest.builder().
            teamId(team.getId()).
            build();
        userService.joinTeam(userId,userJoinTeamRequest);

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

    // 키워드에 해당하는 팀 검색
    public List<TeamSearchResponse> searchTeams(String keyword) {
        List<Team> teams = teamRepository.searchByKeyword(keyword);

        if (teams.isEmpty()) {
            throw new ApiException(TeamStatus.NOTFOUND_TEAM);
        }

        return TeamMapper.toTeamSearchResponseList(teams);
    }

    //전체 팀 조회
    public List<TeamSearchResponse> getAllTeams() {
        List<Team> teams = teamRepository.findAll();
        return TeamMapper.toTeamSearchResponseList(teams);
    }

    //팀 정보 조회
    public TeamResponse getTeamInfo(Long teamId){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ApiException(TeamStatus.NOTFOUND_TEAM));
        return TeamMapper.toTeamResponse(team);
    }

    //팀에 속한 선수들 조회
    public TeamMemberListResponse getTeamMembers(Long teamId){
        List<UserTeam> userTeams = userTeamRepository.findAllByTeamId(teamId);

        List<TeamMemberResponse> userTeamMembers = userTeams.stream().map(userTeam -> {
            Long userId = userTeam.getUser().getId();

            int appearances = matchUserRepository.countAppearances(teamId, userId);
            int goals = matchEventRepository.countEventsByTeamIdAndUserIdAndEventType(teamId,userId,
                MatchEventType.GOAL);
            int warnings = matchEventRepository.countEventsByTeamIdAndUserIdAndEventType(teamId,userId,
                MatchEventType.WARNING);

            return UserTeamMapper.toTeamMemberResponse(userTeam, appearances, goals, warnings);
        }).collect(Collectors.toList());

        return new TeamMemberListResponse(userTeamMembers);
    }

    public boolean existsById(Long teamId) {
    return teamRepository.existsById(teamId);
  }
}
