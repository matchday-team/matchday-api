package com.matchday.matchdayserver.matchuser.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.MatchStatus;
import com.matchday.matchdayserver.common.response.MatchUserStatus;
import com.matchday.matchdayserver.common.response.TeamStatus;
import com.matchday.matchdayserver.common.response.UserStatus;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.match.repository.MatchRepository;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserCreateRequest;
import com.matchday.matchdayserver.matchuser.model.entity.MatchUser;
import com.matchday.matchdayserver.matchuser.model.enums.MatchUserRole;
import com.matchday.matchdayserver.matchuser.model.mapper.MatchUserMapper;
import com.matchday.matchdayserver.matchuser.repository.MatchUserRepository;
import com.matchday.matchdayserver.team.model.entity.Team;
import com.matchday.matchdayserver.team.repository.TeamRepository;
import com.matchday.matchdayserver.user.model.entity.User;
import com.matchday.matchdayserver.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MatchUserService {

  private final MatchUserRepository matchUserRepository;
  private final MatchRepository matchRepository;
  private final UserRepository userRepository;
  private final TeamRepository teamRepository;

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
