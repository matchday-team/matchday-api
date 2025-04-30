package com.matchday.matchdayserver.matchuser.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.MatchUserStatus;
import com.matchday.matchdayserver.common.response.UserStatus;
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
import com.matchday.matchdayserver.user.model.entity.User;
import com.matchday.matchdayserver.user.repository.UserRepository;
import com.matchday.matchdayserver.userteam.model.entity.UserTeam;
import com.matchday.matchdayserver.userteam.repository.UserTeamRepository;
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
    private final MatchEventStrategy matchEventStrategy;

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
    public List<MatchUserResponse> getMatchUser(Long matchId){
      List<MatchUserResponse> matchUserResponses = new ArrayList<>();
      //매치에 속한 유저 정보를 받아온다
      List<MatchUser> matchUsers = matchUserRepository.findByMatchId(matchId);
      //해당 유저의 id를 이용해 유저별 이벤트 통계를 조회해 추가해서 matchUserResponses 만든다
      for(MatchUser matchUser: matchUsers){

        // User
        Long userId=matchUser.getUser().getId();//선수 고유 아이디//todo: N+1 문제 발생 fetch join 해주기
        String userName=matchUser.getUser().getName();//선수 이름

        // userTeam
        Long TeamId=matchUser.getMatch().getId().get
        UserTeam userTeam=matchEventStrategy.findByMatchIdAndUserIdOrThrow
        Integer number=userTeam.getNumber();// 등번호 number

        // MatchUserEventStat
        MatchUserEventStat matchUserEventStat=matchEventQueryService.getMatchUserEventStat(matchId,userId);
        int goals=matchUserEventStat.getGoals();// 해당 경기에서 누적 득점
        int assists=matchUserEventStat.getAssists();// 해당 경기에서 누적 어시스트
        int fouls=matchUserEventStat.getFouls();// 해당 경기에서 누적 파울

        // matchUser
        String matchPosistion=matchUser.getMatchPosition();// 경기 포지션 matchPosition
        String matchGrid=matchUser.getMatchGrid();// 경기 세부 위치 matchGrid

        MatchUserResponse matchUserResponse=MatchUserResponse.builder().
            id(userId).
            name(userName).
            number(number).
            goals(goals).
            assists(assists).
            fouls(fouls).
            matchPosition(matchPosistion).
            matchGrid(matchGrid).
            build();
        matchUserResponses.add(matchUserResponse);
      }
      return matchUserResponses;
    }
}
