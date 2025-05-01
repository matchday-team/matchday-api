package com.matchday.matchdayserver.matchuser.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.MatchUserStatus;
import com.matchday.matchdayserver.common.response.TeamStatus;
import com.matchday.matchdayserver.common.response.UserStatus;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.match.repository.MatchRepository;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserCreateRequest;
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

@Service
@RequiredArgsConstructor
public class MatchUserService {
    private final MatchUserRepository matchUserRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;

    @Transactional
    public void create(Long matchId, MatchUserCreateRequest request){
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ApiException(MatchUserStatus.NOTFOUND_MATCH));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ApiException(UserStatus.NOTFOUND_USER));


        //현재 해당 팀에서 활동 중인지 여부 확인
        UserTeam userTeam = userTeamRepository.findActiveUserInHomeTeam(user.getId(), match.getId())
            .orElseThrow(() -> new ApiException(TeamStatus.INACTIVE_USER_TEAM));


        //중복 등록 여부 확인
        if (matchUserRepository.existsByMatchIdAndUserId(matchId, request.getUserId())) {
          throw new ApiException(MatchUserStatus.ALREADY_REGISTERED);
        }

        MatchUser matchUser = MatchUserMapper.toMatchUser(match, user, request);
        matchUserRepository.save(matchUser);
    }
}
