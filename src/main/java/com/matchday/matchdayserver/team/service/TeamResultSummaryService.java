package com.matchday.matchdayserver.team.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.TeamStatus;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.match.model.enums.MatchResult;
import com.matchday.matchdayserver.match.model.enums.MatchState;
import com.matchday.matchdayserver.match.repository.MatchRepository;
import com.matchday.matchdayserver.matchevent.model.entity.MatchEvent;
import com.matchday.matchdayserver.matchevent.model.enums.MatchEventType;
import com.matchday.matchdayserver.team.model.dto.response.GoalRatio;
import com.matchday.matchdayserver.team.model.dto.response.MatchResultSummary;
import com.matchday.matchdayserver.team.model.dto.response.TeamResultSummaryResponse;
import com.matchday.matchdayserver.team.model.entity.Team;
import com.matchday.matchdayserver.team.repository.TeamRepository;
import com.matchday.matchdayserver.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamResultSummaryService {

    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;

    public TeamResultSummaryResponse getTeamResultSummary(Long teamId) {
        Team team = teamRepository.findById(teamId)
            .orElseThrow(() -> new ApiException(TeamStatus.NOTFOUND_TEAM));

        List<Match> matches = matchRepository.findDistinctByHomeTeamIdOrAwayTeamIdOrderByMatchDateDesc(
            teamId, teamId);

        String mostPlayedPlayerName = userRepository.findMostPlayedPlayerName(teamId);

        List<MatchResult> recentResults = new ArrayList<>();

        List<Match> completedMatches = matches.stream()
            .filter(match -> match.getMatchState() == MatchState.FINISHED)
            .collect(Collectors.toList());

        GoalRatio goalRatio = new GoalRatio();
        MatchResultSummary winLossDraw = new MatchResultSummary();
        TeamResultSummaryResponse teamResultSummaryResponse = new TeamResultSummaryResponse(
            team.getName(),
            team.getTeamImg(),
            matches.size(),
            mostPlayedPlayerName,
            winLossDraw,
            recentResults,
            goalRatio
        );

        for (Match match : completedMatches) {
            int homeGoals = 0;
            int awayGoals = 0;

            for (MatchEvent event : match.getMatchEvents()) {
                if (event.getEventType() == MatchEventType.GOAL) {
                    if (Objects.equals(event.getMatchUser().getTeam().getId(),
                        match.getHomeTeam().getId())) {
                        homeGoals++;
                    } else {
                        awayGoals++;
                    }
                }
            }

            boolean isHomeTeam = Objects.equals(match.getHomeTeam().getId(), teamId);

            if (isHomeTeam) {
                teamResultSummaryResponse = updateTeamResultStats(homeGoals, awayGoals,
                    teamResultSummaryResponse);
            } else {
                teamResultSummaryResponse = updateTeamResultStats(awayGoals, homeGoals,
                    teamResultSummaryResponse);
            }
        }

        return teamResultSummaryResponse;
    }

    private TeamResultSummaryResponse updateTeamResultStats(int teamGoals, int opponentGoals,
        TeamResultSummaryResponse teamResultSummaryResponse) {

        List<MatchResult> recentResults = teamResultSummaryResponse.getRecentMatchResults();
        MatchResultSummary matchResultSummary = teamResultSummaryResponse.getWinLossDraw();
        GoalRatio goalRatio = teamResultSummaryResponse.getGoalRatio();

        goalRatio.incrementGoalsScored(teamGoals);
        goalRatio.incrementGoalsConceded(opponentGoals);

        if (teamGoals > opponentGoals) {
            matchResultSummary.incrementWin(1);
            if (recentResults.size() < 5) {
                recentResults.add(MatchResult.WIN);
            }
        } else if (teamGoals < opponentGoals) {
            matchResultSummary.incrementLoss(1);
            if (recentResults.size() < 5) {
                recentResults.add(MatchResult.LOSE);
            }
        } else {
            matchResultSummary.incrementDraw(1);
            if (recentResults.size() < 5) {
                recentResults.add(MatchResult.DRAW);
            }
        }

        return teamResultSummaryResponse;
    }
}
