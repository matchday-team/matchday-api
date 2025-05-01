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
import com.matchday.matchdayserver.matchuser.model.mapper.MatchUserMapper;
import com.matchday.matchdayserver.matchuser.repository.MatchUserRepository;
import com.matchday.matchdayserver.team.model.entity.Team;
import com.matchday.matchdayserver.team.repository.TeamRepository;
import com.matchday.matchdayserver.user.model.entity.User;
import com.matchday.matchdayserver.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchUserService {
    private final MatchUserRepository matchUserRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public void create(Long matchId, MatchUserCreateRequest request){
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ApiException(MatchUserStatus.NOTFOUND_MATCH));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ApiException(UserStatus.NOTFOUND_USER));
        Team team = teamRepository.findByTeamIdAndUserId(matchId, request.getTeamId())
                .orElseThrow(() -> new ApiException(TeamStatus.NOTFOUND_TEAM));

        if(match.getHomeTeam().getId() != team.getId() && match.getAwayTeam().getId() != team.getId()){
            throw new ApiException(MatchStatus.NOTFOUND_MATCH);
        }

        //중복 등록 여부 확인
        if (matchUserRepository.existsByMatchIdAndUserId(matchId, request.getUserId())) {
          throw new ApiException(MatchUserStatus.ALREADY_REGISTERED);
        }

        MatchUser matchUser = MatchUserMapper.toMatchUser(match, user, team, request);
        matchUserRepository.save(matchUser);
    }
}
