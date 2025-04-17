package com.matchday.matchdayserver.matchplayer.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.MatchPlayerStatus;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.match.repository.MatchRepository;
import com.matchday.matchdayserver.matchplayer.model.dto.MatchPlayerCreateRequest;
import com.matchday.matchdayserver.matchplayer.model.entity.MatchPlayer;
import com.matchday.matchdayserver.matchplayer.repository.MatchPlayerRepository;
import com.matchday.matchdayserver.user.model.entity.User;
import com.matchday.matchdayserver.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchPlayerService {
    private final MatchPlayerRepository matchPlayerRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long create(Long matchId, MatchPlayerCreateRequest request){
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ApiException(MatchPlayerStatus.NOTFOUND_MATCH));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ApiException(MatchPlayerStatus.NOTFOUND_USER));

        MatchPlayer matchPlayer = MatchPlayer.builder()
                .match(match)
                .user(user)
                .role(request.getRole())
                .matchPosition(request.getMatch_position())
                .build();

        matchPlayerRepository.save(matchPlayer);
        return matchPlayer.getId();
    }
}
