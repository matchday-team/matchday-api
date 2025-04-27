package com.matchday.matchdayserver.match.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.MatchStatus;
import com.matchday.matchdayserver.match.model.dto.request.MatchCreateRequest;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.match.repository.MatchRepository;
import com.matchday.matchdayserver.team.model.entity.Team;
import com.matchday.matchdayserver.team.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class MatchCreateService {
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public Long create(MatchCreateRequest request) {
        validateTitle(request.getTitle());
        Team homeTeam = findTeamOrThrow(request.getHomeTeamId());
        Team awayTeam = findTeamOrThrow(request.getAwayTeamId());
        validateTeamsAreDifferent(homeTeam, awayTeam);
        validateStadium(request.getStadium());
        validateDate(request.getMatchDate());
        validateTime(request.getStartTime(), request.getEndTime());

        Match match = Match.builder()
                .title(request.getTitle())
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .matchType(Match.MatchType.valueOf(request.getMatchType().name()))
                .stadium(request.getStadium())
                .matchDate(request.getMatchDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .mainRefereeName(request.getMainRefereeName())
                .assistantReferee1(request.getAssistantReferee1())
                .assistantReferee2(request.getAssistantReferee2())
                .fourthReferee(request.getFourthReferee())
                .build();

        matchRepository.save(match);
        return match.getId();
    }

    // 경기명이 빈 값이면 예외 발생
    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new ApiException(MatchStatus.INVALID_TITLE);
        }
    }

    // 미등록 팀일 경우 예외 발생
    private Team findTeamOrThrow(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new ApiException(MatchStatus.NOTFOUND_TEAM));
    }

    // home 팀과 away 팀이 동일하면 예외 발생
    private void validateTeamsAreDifferent(Team homeTeam, Team awayTeam) {
        if (homeTeam.getId().equals(awayTeam.getId())) {
            throw new ApiException(MatchStatus.SAME_TEAM_ERROR);
        }
    }

    // 경기장 주소가 빈 값이면 예외 발생
    private void validateStadium(String stadium) {
        if (stadium == null || stadium.trim().isEmpty()) {
            throw new ApiException(MatchStatus.INVALID_STADIUM);
        }
    }

    // matchDate가 현재 날짜보다 이전이면 예외 발생
    private void validateDate(LocalDate matchDate) {
        if (matchDate.isBefore(LocalDate.now())) {
            throw new ApiException(MatchStatus.INVALID_DATE);
        }
    }

    //시작시간이 종료시간 보다 늦으면 예외 발생
    private void validateTime(LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new ApiException(MatchStatus.INVALID_TIME);  // 시작 시간이 종료 시간보다 늦을 경우
        }
    }
}
