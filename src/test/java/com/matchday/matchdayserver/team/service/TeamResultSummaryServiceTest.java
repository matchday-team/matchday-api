package com.matchday.matchdayserver.team.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.match.model.entity.MockMatch;
import com.matchday.matchdayserver.match.model.enums.MatchResult;
import com.matchday.matchdayserver.match.model.enums.MatchState;
import com.matchday.matchdayserver.match.repository.MatchRepository;
import com.matchday.matchdayserver.matchevent.model.entity.MatchEvent;
import com.matchday.matchdayserver.matchevent.model.entity.MockMatchEvent;
import com.matchday.matchdayserver.team.model.dto.response.TeamResultSummaryResponse;
import com.matchday.matchdayserver.team.model.entity.MockTeam;
import com.matchday.matchdayserver.team.model.entity.Team;
import com.matchday.matchdayserver.team.repository.TeamRepository;
import com.matchday.matchdayserver.user.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TeamResultSummaryServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TeamResultSummaryService teamResultSummaryService;

    private Team testTeam;
    private Long teamId;

    @BeforeEach
    void setUp() {
        teamId = 1L;
        testTeam = MockTeam.create(teamId);
    }

    @Test
    @DisplayName("팀 결과 요약 조회 - 성공 (홈팀 승리 케이스)")
    void getTeamResultSummary_Success_HomeTeamWin() {
        // given
        List<Match> matches = createMockMatchesHomeTeamWin(testTeam);
        String mostPlayedPlayer = "김선수";

        given(teamRepository.findById(teamId)).willReturn(Optional.of(testTeam));
        given(matchRepository.findDistinctByHomeTeamIdOrAwayTeamIdOrderByMatchDateDesc(teamId,
            teamId))
            .willReturn(matches);
        given(userRepository.findMostPlayedPlayerName(teamId)).willReturn(mostPlayedPlayer);

        // when
        TeamResultSummaryResponse response = teamResultSummaryService.getTeamResultSummary(teamId);

        // then
        assertAll(
            () -> assertEquals("테스트 팀", response.getTeamName()),
            () -> assertEquals(mostPlayedPlayer, response.getMostPlayedPlayerName()),
            () -> assertEquals(1, response.getWinLossDraw().getWin()),
            () -> assertEquals(0, response.getWinLossDraw().getLoss()),
            () -> assertEquals(0, response.getWinLossDraw().getDraw()),
            () -> assertEquals(2, response.getGoalRatio().getGoalsScored()),
            () -> assertEquals(1, response.getGoalRatio().getGoalsConceded()),
            () -> assertEquals(1, response.getRecentMatchResults().size()),
            () -> assertEquals(MatchResult.WIN, response.getRecentMatchResults().get(0))
        );

        verify(teamRepository).findById(teamId);
        verify(matchRepository).findDistinctByHomeTeamIdOrAwayTeamIdOrderByMatchDateDesc(teamId,
            teamId);
        verify(userRepository).findMostPlayedPlayerName(teamId);
    }

    @Test
    @DisplayName("팀 결과 요약 조회 - 성공 (원정팀 무승부 케이스)")
    void getTeamResultSummarySuccessAwayTeamDraw() {
        // given
        List<Match> matches = createMockMatchesAwayTeamDraw();
        String mostPlayedPlayer = "박선수";

        given(teamRepository.findById(teamId)).willReturn(Optional.of(testTeam));
        given(matchRepository.findDistinctByHomeTeamIdOrAwayTeamIdOrderByMatchDateDesc(teamId,
            teamId))
            .willReturn(matches);
        given(userRepository.findMostPlayedPlayerName(teamId)).willReturn(mostPlayedPlayer);

        // when
        TeamResultSummaryResponse response = teamResultSummaryService.getTeamResultSummary(teamId);

        // then
        assertAll(
            () -> assertEquals(0, response.getWinLossDraw().getWin()),
            () -> assertEquals(0, response.getWinLossDraw().getLoss()),
            () -> assertEquals(1, response.getWinLossDraw().getDraw()),
            () -> assertEquals(1, response.getGoalRatio().getGoalsScored()),
            () -> assertEquals(1, response.getGoalRatio().getGoalsConceded()),
            () -> assertEquals(MatchResult.DRAW, response.getRecentMatchResults().get(0))
        );
    }

    @Test
    @DisplayName("팀 결과 요약 조회 - 성공 (복합 경기 결과)")
    void getTeamResultSummary_Success_MultipleMatches() {
        // given
        List<Match> matches = createMockMatches_MultipleResults();
        String mostPlayedPlayer = "이선수";

        given(teamRepository.findById(teamId)).willReturn(Optional.of(testTeam));
        given(matchRepository.findDistinctByHomeTeamIdOrAwayTeamIdOrderByMatchDateDesc(teamId,
            teamId))
            .willReturn(matches);
        given(userRepository.findMostPlayedPlayerName(teamId)).willReturn(mostPlayedPlayer);

        // when
        TeamResultSummaryResponse response = teamResultSummaryService.getTeamResultSummary(teamId);

        // then
        assertAll(
            () -> assertEquals(1, response.getWinLossDraw().getWin()),
            () -> assertEquals(1, response.getWinLossDraw().getLoss()),
            () -> assertEquals(1, response.getWinLossDraw().getDraw()),
            () -> assertEquals(4, response.getGoalRatio().getGoalsScored()),
            () -> assertEquals(3, response.getGoalRatio().getGoalsConceded()),
            () -> assertEquals(3, response.getRecentMatchResults().size())
        );
    }

    @Test
    @DisplayName("팀 결과 요약 조회 - 실패 (존재하지 않는 팀)")
    void getTeamResultSummary_Fail_TeamNotFound() {
        // given
        given(teamRepository.findById(teamId)).willReturn(Optional.empty());

        // when & then
        assertThrows(ApiException.class, () ->
            teamResultSummaryService.getTeamResultSummary(teamId)
        );

        verify(teamRepository).findById(teamId);
        verifyNoInteractions(matchRepository, userRepository);
    }

    @Test
    @DisplayName("팀 결과 요약 조회 - 성공 (진행 중인 경기 제외)")
    void getTeamResultSummary_Success_ExcludeOngoingMatches() {
        // given
        List<Match> matches = createMockMatchesWithOngoingMatch();
        String mostPlayedPlayer = "최선수";

        given(teamRepository.findById(teamId)).willReturn(Optional.of(testTeam));
        given(matchRepository.findDistinctByHomeTeamIdOrAwayTeamIdOrderByMatchDateDesc(teamId,
            teamId))
            .willReturn(matches);
        given(userRepository.findMostPlayedPlayerName(teamId)).willReturn(mostPlayedPlayer);

        // when
        TeamResultSummaryResponse response = teamResultSummaryService.getTeamResultSummary(teamId);

        // then
        // 진행 중인 경기는 계산에서 제외되어야 함
        assertAll(
            () -> assertEquals(1, response.getWinLossDraw().getWin()),
            () -> assertEquals(0, response.getWinLossDraw().getLoss()),
            () -> assertEquals(0, response.getWinLossDraw().getDraw()),
            () -> assertEquals(1, response.getRecentMatchResults().size())
        );
    }

    // Mock 데이터 생성 메서드들

    private List<Match> createMockMatchesAwayTeamDraw() {
        Team opponentTeam = MockTeam.create(2L);
        Match match = MockMatch.create(1L, opponentTeam, testTeam, MatchState.FINISHED);

        // 양팀 각각 1골씩
        List<MatchEvent> events = Arrays.asList(
            MockMatchEvent.create(testTeam), // 원정팀(테스트팀) 골
            MockMatchEvent.create(opponentTeam) // 홈팀 골
        );

        lenient().when(match.getMatchEvents()).thenReturn(events);

        return Arrays.asList(match);
    }

    private List<Match> createMockMatches_MultipleResults() {
        Team opponent1 = MockTeam.create(2L);
        Team opponent2 = MockTeam.create(3L);
        Team opponent3 = MockTeam.create(4L);

        // 승리 경기 (홈팀으로): 테스트팀 2골 vs 상대팀 0골
        Match winMatch = MockMatch.create(1L, testTeam, opponent1, MatchState.FINISHED);
        List<MatchEvent> winEvents = Arrays.asList(
            MockMatchEvent.create(testTeam),
            MockMatchEvent.create(testTeam)
        );
        lenient().when(winMatch.getMatchEvents()).thenReturn(winEvents);

        // 패배 경기 (원정팀으로): 테스트팀 1골 vs 상대팀 2골
        Match lossMatch = MockMatch.create(2L, opponent2, testTeam, MatchState.FINISHED);
        List<MatchEvent> lossEvents = Arrays.asList(
            MockMatchEvent.create(opponent2),
            MockMatchEvent.create(opponent2),
            MockMatchEvent.create(testTeam)
        );
        lenient().when(lossMatch.getMatchEvents()).thenReturn(lossEvents);

        // 무승부 경기 (홈팀으로): 테스트팀 1골 vs 상대팀 1골  
        Match drawMatch = MockMatch.create(3L, testTeam, opponent3, MatchState.FINISHED);
        List<MatchEvent> drawEvents = Arrays.asList(
            MockMatchEvent.create(testTeam),
            MockMatchEvent.create(opponent3)
        );
        lenient().when(drawMatch.getMatchEvents()).thenReturn(drawEvents);

        return Arrays.asList(winMatch, lossMatch, drawMatch);
    }

    private List<Match> createMockMatchesWithOngoingMatch() {
        Team opponent1 = MockTeam.create(2L);
        Team opponent2 = MockTeam.create(3L);

        // 완료된 경기
        Match finishedMatch = MockMatch.create(1L, testTeam, opponent1, MatchState.FINISHED);
        List<MatchEvent> finishedEvents = Arrays.asList(MockMatchEvent.create(testTeam));
        lenient().when(finishedMatch.getMatchEvents()).thenReturn(finishedEvents);

        // 진행 중인 경기 (계산에서 제외되어야 함)
        Match ongoingMatch = MockMatch.create(2L, testTeam, opponent2, MatchState.PLAY_FIRST_HALF);
        List<MatchEvent> ongoingEvents = Arrays.asList(
            MockMatchEvent.create(testTeam),
            MockMatchEvent.create(testTeam)
        );
        lenient().when(ongoingMatch.getMatchEvents()).thenReturn(ongoingEvents);

        return Arrays.asList(finishedMatch, ongoingMatch);
    }

    private List<Match> createMockMatchesHomeTeamWin(Team testTeam) {
        Team opponentTeam = MockTeam.create(2L);
        Match match = MockMatch.create(1L, testTeam, opponentTeam, MatchState.FINISHED);

        // 홈팀(테스트팀) 2골, 원정팀 1골
        List<MatchEvent> events = Arrays.asList(
            MockMatchEvent.create(testTeam), // 홈팀 골
            MockMatchEvent.create(testTeam), // 홈팀 골
            MockMatchEvent.create(opponentTeam) // 원정팀 골
        );

        lenient().when(match.getMatchEvents()).thenReturn(events);

        return Arrays.asList(match);
    }


}