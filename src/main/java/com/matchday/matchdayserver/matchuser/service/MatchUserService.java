package com.matchday.matchdayserver.matchuser.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.MatchStatus;
import com.matchday.matchdayserver.common.response.MatchUserStatus;
import com.matchday.matchdayserver.common.response.TeamStatus;
import com.matchday.matchdayserver.common.response.UserStatus;
import com.matchday.matchdayserver.common.response.UserTeamStatus;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.match.repository.MatchRepository;
import com.matchday.matchdayserver.matchevent.service.MatchEventQueryService;
import com.matchday.matchdayserver.matchevent.service.MatchEventStrategy;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserCreateRequest;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserEventStat;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserResponse;
import com.matchday.matchdayserver.matchuser.model.entity.MatchUser;
import com.matchday.matchdayserver.matchuser.model.mapper.MatchUserMapper;
import com.matchday.matchdayserver.matchuser.repository.MatchUserRepository;
import com.matchday.matchdayserver.team.repository.TeamRepository;
import com.matchday.matchdayserver.user.model.entity.User;
import com.matchday.matchdayserver.user.repository.UserRepository;
import com.matchday.matchdayserver.userteam.model.entity.UserTeam;
import com.matchday.matchdayserver.userteam.repository.UserTeamRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchUserService {
    private final MatchUserRepository matchUserRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final MatchEventQueryService matchEventQueryService;
    private final UserTeamRepository userTeamRepository;

    @Transactional
    public void create(Long matchId, MatchUserCreateRequest request){
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ApiException(MatchUserStatus.NOTFOUND_MATCH));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ApiException(UserStatus.NOTFOUND_USER));

        MatchUser matchUser = MatchUserMapper.toMatchUser(match, user, request);
        matchUserRepository.save(matchUser);
    }

    //매치에 속한 선수들의 정보들을 조회
    public List<MatchUserResponse> getMatchUsers(Long matchId,Long teamId){
      //검증
      Match match = matchRepository.findById(matchId).orElseThrow(() -> new ApiException(MatchUserStatus.NOTFOUND_MATCH));
      validateTeamParticipation(match, teamId);

      //매치에 속한 유저들의 정보 받아온다
      List<MatchUser> matchUsers = matchUserRepository.findByMatchId(matchId);

      //해당 유저의 id를 이용해 유저별 이벤트 통계를 조회해 추가해서 matchUserResponses 만든다
      List<MatchUserResponse> matchUserResponses = new ArrayList<>();
      for(MatchUser matchUser: matchUsers){
        Long userId=matchUser.getUser().getId();//선수 고유 아이디 todo: N+1 문제 발생시 fetch join 해주기

        // userTeam
        Optional<UserTeam> userTeamOpt = userTeamRepository.findByUserIdAndTeamId(userId, teamId);
        if (userTeamOpt.isEmpty()) continue; // 팀에 해당하는 유저가 아니면 pass

        // MatchUserEventStat
        MatchUserEventStat stat=matchEventQueryService.getMatchUserEventStat(matchId,userId);
        // MatchUser + UserTeam + MatchUserEventStat → MatchUserResponse (DTO)변환
        matchUserResponses.add(MatchUserMapper.toMatchUserResponse(matchUser,userTeamOpt.get(),stat));
      }
      return matchUserResponses;
    }

    private void validateTeamParticipation(Match match, Long teamId) {//입력한 teamId가 홈팀,어웨이팀중 하나가 맞는지 검증
      boolean isParticipant = teamId.equals(match.getHomeTeam().getId()) ||
          teamId.equals(match.getAwayTeam().getId());
      if (!isParticipant) {
        throw new ApiException(MatchStatus.TEAM_NOT_PARTICIPATING);
      }
    }
}
