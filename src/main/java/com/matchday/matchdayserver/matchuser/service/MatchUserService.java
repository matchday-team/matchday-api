package com.matchday.matchdayserver.matchuser.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.MatchUserStatus;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.match.repository.MatchRepository;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserCreateRequest;
import com.matchday.matchdayserver.matchuser.model.entity.MatchUser;
import com.matchday.matchdayserver.matchuser.repository.MatchUserRepository;
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

    @Transactional
    public void create(Long matchId, MatchUserCreateRequest request){
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ApiException(MatchUserStatus.NOTFOUND_MATCH));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ApiException(MatchUserStatus.NOTFOUND_USER));

        MatchUser matchPlayer = MatchUser.builder()
                .match(match)
                .user(user)
                .role(request.getRole())
                .matchPosition(request.getMatchPosition())
                .matchGrid(request.getMatchGrid())
                .build();

        matchUserRepository.save(matchPlayer);
    }
}
