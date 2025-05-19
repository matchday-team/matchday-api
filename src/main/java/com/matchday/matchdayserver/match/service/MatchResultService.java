package com.matchday.matchdayserver.match.service;

import com.matchday.matchdayserver.match.model.dto.response.MatchResultInfoResponse;
import com.matchday.matchdayserver.match.model.dto.response.MatchScoreResponse;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.match.model.enums.MatchResult;
import com.matchday.matchdayserver.match.repository.MatchRepository;
import com.matchday.matchdayserver.matchuser.model.entity.MatchUser;
import com.matchday.matchdayserver.matchuser.repository.MatchUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchResultService {

    private final MatchRepository matchRepository;
    private final MatchScoreService matchScoreService;
    private final MatchUserRepository matchUserRepository;

    public List<MatchResultInfoResponse> getMatchResultsByTeam(Long teamId) {
        List<Match> matches = matchRepository.findAllByHomeTeamIdOrAwayTeamId(teamId, teamId);

        // 2. 각 경기마다 팀 기준으로 결과 계산 후 DTO 리스트 생성
        return matches.stream()
            .map(match -> {
                Long matchId = match.getId();

                MatchScoreResponse scoreResponse = matchScoreService.getMatchScore(matchId);
                MatchResult matchResult = calculateMatchResult(scoreResponse, teamId, match);
                int matchTime = calculateActualMatchTime(match);
                int playerCount = countPlayersOnField(matchId);

                return MatchResultInfoResponse.builder()
                    .matchId(matchId)
                    .homeTeamId(match.getHomeTeam().getId())
                    .homeTeamName(match.getHomeTeam().getName())
                    .awayTeamId(match.getAwayTeam().getId())
                    .awayTeamName(match.getAwayTeam().getName())
                    .matchTitle(match.getTitle())
                    .matchResult(matchResult)
                    .homeScore(scoreResponse.getHomeScore().getGoalCount())
                    .awayScore(scoreResponse.getAwayScore().getGoalCount())
                    .matchTime(matchTime)
                    .playerCount(playerCount)
                    .stadium(match.getStadium())
                    .matchDate(match.getMatchDate())
                    .build();
            })
            .collect(Collectors.toList());
    }

    // 경기 결과 계산 (팀 입장 기준)
    private MatchResult calculateMatchResult(MatchScoreResponse score, Long teamId, Match match) {
        int homeGoals = score.getHomeScore().getGoalCount();
        int awayGoals = score.getAwayScore().getGoalCount();

        MatchResult overallResult;
        if (homeGoals > awayGoals) {
            overallResult = MatchResult.WIN;
        } else if (homeGoals < awayGoals) {
            overallResult = MatchResult.LOSE;
        } else {
            overallResult = MatchResult.DRAW;
        }

        boolean isHomeTeam = match.getHomeTeam().getId().equals(teamId);

        if (overallResult == MatchResult.DRAW) {
            return MatchResult.DRAW;
        }

        if (overallResult == MatchResult.WIN) {
            return isHomeTeam ? MatchResult.WIN : MatchResult.LOSE;
        } else {
            return isHomeTeam ? MatchResult.LOSE : MatchResult.WIN;
        }
    }

    // 경기 실제 시간 계산
    public int calculateActualMatchTime(Match match) {
        int firstHalfMinutes = 0;
        int secondHalfMinutes = 0;

        LocalTime firstStart = match.getFirstHalfStartTime();
        LocalTime firstEnd = match.getFirstHalfEndTime();
        LocalTime secondStart = match.getSecondHalfStartTime();
        LocalTime secondEnd = match.getSecondHalfEndTime();

        if (firstStart != null && firstEnd != null) {
            firstHalfMinutes = (int) Duration.between(firstStart, firstEnd).toMinutes();
        }

        if (secondStart != null && secondEnd != null) {
            secondHalfMinutes = (int) Duration.between(secondStart, secondEnd).toMinutes();
        }

        return firstHalfMinutes + secondHalfMinutes;
    }

    // 출전 선수 수 조회
    public int countPlayersOnField(Long matchId) {
        List<MatchUser> matchUsers = matchUserRepository.findByMatchId(matchId);

        return (int) matchUsers.stream()
            .filter(mu -> mu.getMatchPosition() != null && mu.getMatchGrid() != null)
            .count();
    }
}
