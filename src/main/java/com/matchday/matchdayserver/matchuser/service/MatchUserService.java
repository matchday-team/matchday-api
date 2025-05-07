package com.matchday.matchdayserver.matchuser.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.MatchStatus;
import com.matchday.matchdayserver.common.response.MatchUserStatus;
import com.matchday.matchdayserver.common.response.TeamStatus;
import com.matchday.matchdayserver.common.response.UserStatus;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.match.repository.MatchRepository;
import com.matchday.matchdayserver.matchevent.service.MatchEventQueryService;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserCreateRequest;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserEventStat;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserGridUpdateRequest;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserGroupResponse;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserResponse;
import com.matchday.matchdayserver.matchuser.model.entity.MatchUser;
import com.matchday.matchdayserver.matchuser.model.enums.MatchUserRole;
import com.matchday.matchdayserver.matchuser.model.mapper.MatchUserMapper;
import com.matchday.matchdayserver.matchuser.repository.MatchUserRepository;
import com.matchday.matchdayserver.team.model.entity.Team;
import com.matchday.matchdayserver.team.repository.TeamRepository;
import com.matchday.matchdayserver.user.model.entity.User;
import com.matchday.matchdayserver.user.repository.UserRepository;
import com.matchday.matchdayserver.userteam.model.entity.UserTeam;
import com.matchday.matchdayserver.userteam.repository.UserTeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchUserService {

  private final MatchUserRepository matchUserRepository;
  private final MatchRepository matchRepository;
  private final UserRepository userRepository;
  private final TeamRepository teamRepository;
  private final MatchEventQueryService matchEventQueryService;
  private final UserTeamRepository userTeamRepository;

  @Transactional
  public Long create(Long matchId, MatchUserCreateRequest request) {
    Match match = matchRepository.findById(matchId)
        .orElseThrow(() -> new ApiException(MatchUserStatus.NOTFOUND_MATCH));
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new ApiException(UserStatus.NOTFOUND_USER));

    Team team = null;

    if (!canIgnoreTeamConstraints(request.getRole())) {
      team = teamRepository.findById(request.getTeamId())
          .orElseThrow(() -> new ApiException(TeamStatus.NOTFOUND_TEAM));
    }

    if (isTeamNotInMatch(team, match)) {
      throw new ApiException(MatchStatus.NOTFOUND_MATCH);
    }
    //중복 등록 여부 확인
    if (matchUserRepository.existsByMatchIdAndUserId(matchId, request.getUserId())) {
      throw new ApiException(MatchUserStatus.ALREADY_REGISTERED);
    }

    MatchUser matchUser = MatchUserMapper.toMatchUser(match, user, team, request);
    matchUserRepository.save(matchUser);
    return matchUser.getId();
  }

  @Transactional
  public void updateMatchUserGrid(Long matchUserId, MatchUserGridUpdateRequest matchUserGridUpdateRequest){
      MatchUser matchUser = matchUserRepository.findById(matchUserId)
          .orElseThrow(() -> new ApiException(MatchUserStatus.NOTFOUND_MATCHUSER));

      matchUser.updateMatchGrid(matchUserGridUpdateRequest.getMatchGridX(), matchUserGridUpdateRequest.getMatchGridY());
     matchUserRepository.save(matchUser);
  }

    @Transactional
    public MatchUserGroupResponse getGroupedMatchUsers(Long matchId) {
        List<MatchUser> matchUsers = matchUserRepository.findByMatchId(matchId);
        Match match = matchRepository.findById(matchId)
            .orElseThrow(() -> new ApiException(MatchStatus.NOTFOUND_MATCH));
        Long homeTeamId = match.getHomeTeam().getId();
        Long awayTeamId = match.getAwayTeam().getId();

        List<MatchUserResponse> homeStarters = new ArrayList<>();
        List<MatchUserResponse> homeSubstitutes = new ArrayList<>();
        List<MatchUserResponse> awayStarters = new ArrayList<>();
        List<MatchUserResponse> awaySubstitutes = new ArrayList<>();

        for (MatchUser matchUser : matchUsers) {
            Long userId = matchUser.getUser().getId();
            String userName = matchUser.getUser().getName();
            Long teamId = matchUser.getTeam().getId();

            Optional<UserTeam> optionalUserTeam = userTeamRepository.findActiveUserTeamByUserIdAndTeamId(userId, teamId);
            if (optionalUserTeam.isEmpty()) {
                continue; // 활동 중이 아니거나 기록이 없으면 무시
            }

            Integer number = optionalUserTeam.get().getNumber();

            MatchUserEventStat stat = matchEventQueryService.getMatchUserEventStat(matchId, matchUser.getId());

            MatchUserResponse response = MatchUserResponse.builder()
                .id(userId)
                .name(userName)
                .number(number)
                .matchPosition(matchUser.getMatchPosition())
                .matchGridX(matchUser.getMatchGridX())
                .matchGridY(matchUser.getMatchGridY())
                .goals(stat.getGoals())
                .assists(stat.getAssists())
                .yellowCards(stat.getYellowCards())
                .redCards(stat.getRedCards())
                .caution(stat.getCaution())
                .sentOff(stat.isSentOff())  // 퇴장 여부 추가
                .build();

            if (teamId.equals(homeTeamId)) {
                if (matchUser.getRole() == MatchUserRole.START_PLAYER) {
                    homeStarters.add(response);
                } else if (matchUser.getRole() == MatchUserRole.SUB_PLAYER) {
                    homeSubstitutes.add(response);
                }
            } else if (teamId.equals(awayTeamId)) {
                if (matchUser.getRole() == MatchUserRole.START_PLAYER) {
                    awayStarters.add(response);
                } else if (matchUser.getRole() == MatchUserRole.SUB_PLAYER) {
                    awaySubstitutes.add(response);
                }
            }
        }

        return MatchUserGroupResponse.builder()
            .homeTeam(MatchUserGroupResponse.TeamGroupedUsers.builder()
                .starters(homeStarters)
                .substitutes(homeSubstitutes)
                .build())
            .awayTeam(MatchUserGroupResponse.TeamGroupedUsers.builder()
                .starters(awayStarters)
                .substitutes(awaySubstitutes)
                .build())
            .build();
    }


  /**
   * 팀이 매치에 포함되어 있지 않은지 확인하는 메서드
   *
   * @param team
   * @param match
   * @return true: 팀이 매치에 포함되어 있지 않음, false: 팀이 매치에 포함되어 있음
   */
  private static boolean isTeamNotInMatch(Team team, Match match) {
    if (team == null) {
      return false; // 팀이 null이면 조건 검사를 하지 않음
    }

    boolean isHomeTeam = match.getHomeTeam() != null &&
        Objects.equals(match.getHomeTeam().getId(), team.getId());
    boolean isAwayTeam = match.getAwayTeam() != null &&
        Objects.equals(match.getAwayTeam().getId(), team.getId());

    return !isHomeTeam && !isAwayTeam;
  }

  private boolean canIgnoreTeamConstraints(MatchUserRole matchUserRole) {
    return matchUserRole == MatchUserRole.ADMIN || matchUserRole == MatchUserRole.ARCHIVES;
  }
}
