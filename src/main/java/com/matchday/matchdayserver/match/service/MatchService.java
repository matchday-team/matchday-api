package com.matchday.matchdayserver.match.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.MatchStatus;
import com.matchday.matchdayserver.common.response.TeamStatus;
import com.matchday.matchdayserver.match.model.dto.request.MatchHalfTimeRequest;
import com.matchday.matchdayserver.match.model.dto.response.MatchInfoResponse;
import com.matchday.matchdayserver.match.model.dto.response.MatchListPageResponse;
import com.matchday.matchdayserver.match.model.dto.response.MatchListResponse;
import com.matchday.matchdayserver.match.model.dto.response.MatchScoreResponse;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.match.model.enums.HalfType;
import com.matchday.matchdayserver.match.model.enums.MatchState;
import com.matchday.matchdayserver.match.model.enums.TimeType;
import com.matchday.matchdayserver.match.model.mapper.MatchMapper;
import com.matchday.matchdayserver.match.repository.MatchRepository;
import com.matchday.matchdayserver.match.model.dto.request.MatchMemoRequest;
import com.matchday.matchdayserver.match.model.dto.response.MatchMemoResponse;
import com.matchday.matchdayserver.team.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {
  private final MatchRepository matchRepository;
  private final MatchScoreService matchScoreService;
    private final TeamRepository teamRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public MatchInfoResponse getMatchInfo(Long matchId) {
    Match match = matchRepository.findById(matchId)
        .orElseThrow(() -> new ApiException(MatchStatus.NOTFOUND_MATCH));
    return MatchMapper.toResponse(match);
  }

  @Transactional
  public void createOrUpdate(Long matchId, MatchMemoRequest request) {
    Match match = matchRepository.findById(matchId)
        .orElseThrow(() -> new ApiException(MatchStatus.NOTFOUND_MATCH));

    match.updateMemo(request.getMemo());
    matchRepository.save(match);
      MatchMemoResponse response = MatchMemoResponse.builder().
          matchId(match.getId()).
          memo(match.getMemo()).
          build();
      simpMessagingTemplate.convertAndSend("/topic/match-memo", response);
  }

  public MatchMemoResponse get(Long matchId) {
    Match match = matchRepository.findById(matchId)
        .orElseThrow(() -> new ApiException(MatchStatus.NOTFOUND_MATCH));

    return MatchMemoResponse.builder()
        .memo(match.getMemo())
        .build();
  }


  private void validateTeamParticipation(Match match, Long teamId) {//입력한 teamId가 홈팀,어웨이팀중 하나가 맞는지 검증
    boolean isParticipant = teamId.equals(match.getHomeTeam().getId()) ||
        teamId.equals(match.getAwayTeam().getId());
    if (!isParticipant) {
      throw new ApiException(MatchStatus.TEAM_NOT_PARTICIPATING);
    }
  }

  public List<MatchListResponse> getMatchListByTeam(Long teamId) {
      teamRepository.findById(teamId)
          .orElseThrow(() -> new ApiException(TeamStatus.NOTFOUND_TEAM));

      List<Match> matches = matchRepository.findByHomeTeamIdOrAwayTeamId(teamId, teamId);

      return matches.stream()
          .map(match -> {
              MatchScoreResponse scoreRes = matchScoreService.getMatchScore(match.getId());
              return MatchMapper.toMatchListResponse(match, scoreRes);
          })
          .collect(Collectors.toList());
  }

  public MatchListPageResponse getMatchList(int page, int size) {
      // 유효성 검사
      if (page < 0) {
          throw new ApiException(MatchStatus.INVALID_PAGE_NUMBER);
      }
      if (size <= 0) {
          throw new ApiException(MatchStatus.INVALID_PAGE_SIZE);
      }

      long totalCount = matchRepository.count();

      // 데이터가 없는 경우 빈 응답 반환
      if (totalCount == 0) {
          return MatchListPageResponse.builder()
              .totalCount(0)
              .matches(List.of())
              .build();
      }

        int offset = page * size;

        List<Match> matches = matchRepository.findMatchesByOffsetAndLimit(offset, size);

        List<MatchListResponse> responseList = matches.stream()
            .map(match -> {
                MatchScoreResponse scoreRes = matchScoreService.getMatchScore(match.getId());
                return MatchMapper.toMatchListResponse(match, scoreRes);
            })
            .collect(Collectors.toList());

        return MatchListPageResponse.builder()
            .totalCount(totalCount)
            .matches(responseList)
            .build();
    }

    @Transactional
    public void setHalfTime(Long id, MatchHalfTimeRequest halfTimeRequest) {
        Match match = matchRepository.findById(id)
            .orElseThrow(() -> new ApiException(MatchStatus.NOTFOUND_MATCH));

        validateTime(match, halfTimeRequest);

        switch (halfTimeRequest.getHalfType()) {
            case FIRST_HALF:
                switch (halfTimeRequest.getTimeType()) {
                    case START_TIME:
                        match.setFirstHalfStartTime(halfTimeRequest.getTime());
                        match.setMatchState(MatchState.IN_PLAY);
                        break;
                    case END_TIME:
                        match.setFirstHalfEndTime(halfTimeRequest.getTime());
                        break;
                }
                break;

            case SECOND_HALF:
                switch (halfTimeRequest.getTimeType()) {
                    case START_TIME:
                        match.setSecondHalfStartTime(halfTimeRequest.getTime());
                        break;
                    case END_TIME:
                        match.setSecondHalfEndTime(halfTimeRequest.getTime());
                        match.setMatchState(MatchState.FINISHED);
                        break;
                }
                break;
        }

        matchRepository.save(match);
    }


    private void validateTime(Match match, MatchHalfTimeRequest halfTimeRequest) {
        LocalTime newTime = halfTimeRequest.getTime();

        HalfType halfType = halfTimeRequest.getHalfType();
        TimeType timeType = halfTimeRequest.getTimeType();

        switch (halfType) {
            case FIRST_HALF -> {
                LocalTime start = match.getFirstHalfStartTime();
                LocalTime end = match.getFirstHalfEndTime();

                if (timeType == TimeType.START_TIME && end != null && newTime.isAfter(end)) {
                    throw new ApiException(MatchStatus.TIME_ORDER_INVALID); // 시작 > 종료
                }
                if (timeType == TimeType.END_TIME && start != null && newTime.isBefore(start)) {
                    throw new ApiException(MatchStatus.INVALID_TIME_RANGE); // 종료 < 시작
                }
            }

            case SECOND_HALF -> {
                LocalTime start = match.getSecondHalfStartTime();
                LocalTime end = match.getSecondHalfEndTime();
                LocalTime firstHalfEnd = match.getFirstHalfEndTime();

                if (timeType == TimeType.START_TIME) {
                    if (end != null && newTime.isAfter(end)) {
                        throw new ApiException(MatchStatus.TIME_ORDER_INVALID); // 시작 > 종료
                    }
                    if (firstHalfEnd != null && newTime.isBefore(firstHalfEnd)) {
                        throw new ApiException(MatchStatus.SECOND_HALF_TIME_ERROR); // 후반 시작 < 전반 종료
                    }
                }

                if (timeType == TimeType.END_TIME && start != null && newTime.isBefore(start)) {
                    throw new ApiException(MatchStatus.INVALID_TIME_RANGE); // 종료 < 시작
                }
            }

            default -> throw new ApiException(MatchStatus.INVALID_HALF_TYPE); // 잘못된 HALF 타입
        }
    }
}
