package com.matchday.matchdayserver.matchevent.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.model.Message;
import com.matchday.matchdayserver.common.response.MatchStatus;
import com.matchday.matchdayserver.common.response.TeamStatus;
import com.matchday.matchdayserver.common.response.UserStatus;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.match.repository.MatchRepository;
import com.matchday.matchdayserver.matchevent.mapper.MatchEventMapper;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventRequest;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventResponse;
import com.matchday.matchdayserver.matchevent.model.entity.MatchEvent;
import com.matchday.matchdayserver.user.model.entity.User;
import com.matchday.matchdayserver.user.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.matchday.matchdayserver.matchevent.repository.MatchEventRepository;
import com.matchday.matchdayserver.matchuser.model.entity.MatchUser;
import com.matchday.matchdayserver.matchuser.repository.MatchUserRepository;
import com.matchday.matchdayserver.team.model.entity.Team;
import com.matchday.matchdayserver.team.repository.TeamRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchEventSaveService {

    private final MatchUserRepository matchUserRepository;
    private final MatchEventRepository matchEventRepository;
    private final TeamRepository teamRepository;

    public MatchEventResponse saveMatchEvent(Message<MatchEventRequest> request) {
        Long userId = Long.parseLong(request.getToken());

        MatchUser matchUser = matchUserRepository
                .findByMatchIdAndUserIdWithFetch(request.getData().getMatchId(), userId)
                .orElseThrow(() -> new ApiException(MatchStatus.NOT_PARTICIPATING_PLAYER));
        if (!matchUser.getRole().equals(MatchUser.Role.ARCHIVES)) {
            new ApiException(MatchStatus.UNAUTHORIZED_RECORD);
        }

        User user = matchUser.getUser();
        Match match = matchUser.getMatch();

        Team team = teamRepository.findByMatchIdAndUserId(request.getData().getMatchId(), userId)
                .orElseThrow(() -> new ApiException(TeamStatus.NOTFOUND_TEAM));

        MatchEvent matchEvent = MatchEventMapper.toEntity(request.getData(), match, user);
        matchEventRepository.save(matchEvent);

        return MatchEventMapper.toResponse(matchEvent, team);
    }
}
