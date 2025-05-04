package com.matchday.matchdayserver.match.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.MatchStatus;
import com.matchday.matchdayserver.common.response.TeamStatus;
import com.matchday.matchdayserver.match.model.dto.request.MatchHalfTimeRequest;
import com.matchday.matchdayserver.match.model.dto.response.MatchInfoResponse;
import com.matchday.matchdayserver.match.model.dto.response.MatchListResponse;
import com.matchday.matchdayserver.match.model.dto.response.MatchScoreResponse;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.match.model.enums.MatchState;
import com.matchday.matchdayserver.match.model.mapper.MatchMapper;
import com.matchday.matchdayserver.match.repository.MatchRepository;
import com.matchday.matchdayserver.match.model.dto.request.MatchMemoRequest;
import com.matchday.matchdayserver.match.model.dto.response.MatchMemoResponse;
import com.matchday.matchdayserver.team.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    public MatchInfoResponse getMatchInfo(Long matchId) {
    Match match = matchRepository.findById(matchId)
        .orElseThrow(() -> new ApiException(MatchStatus.NOTFOUND_MATCH));
    return MatchMapper.toResponse(match);
  }

  @Transactional
  public void createOrUpdate(Long matchId, Long teamId, MatchMemoRequest request) {
    Match match = matchRepository.findById(matchId)
        .orElseThrow(() -> new ApiException(MatchStatus.NOTFOUND_MATCH));
    validateTeamParticipation(match, teamId);

    if(teamId.equals(match.getAwayTeam().getId())){
      match.updateAwayTeamMemo(request.getMemo());
    }
    else if(teamId.equals(match.getHomeTeam().getId())){
      match.updateHomeTeamMemo(request.getMemo());
    }

    matchRepository.save(match);
  }

  public MatchMemoResponse get(Long matchId, Long teamId) {
    Match match = matchRepository.findById(matchId)
        .orElseThrow(() -> new ApiException(MatchStatus.NOTFOUND_MATCH));

    validateTeamParticipation(match, teamId);

    String memo;
    if (teamId.equals(match.getAwayTeam().getId())) {
      memo = match.getAwayTeamMemo();
    } else if (teamId.equals(match.getHomeTeam().getId())) {
      memo = match.getHomeTeamMemo();
    } else {
      throw new ApiException(TeamStatus.NOTFOUND_TEAM);
    }

    return MatchMemoResponse.builder()
        .matchId(matchId)
        .teamId(teamId)
        .memo(memo)
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

    @Transactional
    public void setHalfTime(Long id, String halfType, MatchHalfTimeRequest halfTimeRequest) {
        Match match = matchRepository.findById(id)
            .orElseThrow(() -> new ApiException(MatchStatus.NOTFOUND_MATCH));

        if ("first".equalsIgnoreCase(halfType)) {
            validateFirstHalfTime(match, halfTimeRequest);
            if (halfTimeRequest.getStartTime() != null) {
                match.setFirstHalfStartTime(halfTimeRequest.getStartTime());
            }
            if (halfTimeRequest.getEndTime() != null) {
                match.setFirstHalfEndTime(halfTimeRequest.getEndTime());
            }
        } else if ("second".equalsIgnoreCase(halfType)) {
            validateSecondHalfTime(match, halfTimeRequest);
            if (halfTimeRequest.getStartTime() != null) {
                match.setSecondHalfStartTime(halfTimeRequest.getStartTime());
            }
            if (halfTimeRequest.getEndTime() != null) {
                match.setSecondHalfEndTime(halfTimeRequest.getEndTime());
            }
        } else {
            throw new ApiException(MatchStatus.INVALID_HALF_TYPE);
        }

        matchRepository.save(match);
    }

    private void validateFirstHalfTime(Match match, MatchHalfTimeRequest request) {
        LocalTime currentFirstHalfEnd = match.getFirstHalfEndTime();
        LocalTime currentFirstHalfStart = match.getFirstHalfStartTime();
        LocalTime newStart = request.getStartTime();
        LocalTime newEnd = request.getEndTime();

        if (newStart != null && currentFirstHalfEnd != null && newStart.isAfter(currentFirstHalfEnd)) {
            throw new ApiException(MatchStatus.TIME_ORDER_INVALID);
        }

        if (newEnd != null && currentFirstHalfStart != null && newEnd.isBefore(currentFirstHalfStart)) {
            throw new ApiException(MatchStatus.INVALID_TIME_RANGE);
        }
    }

    private void validateSecondHalfTime(Match match, MatchHalfTimeRequest request) {
        LocalTime currentSecondHalfEnd = match.getSecondHalfEndTime();
        LocalTime currentSecondHalfStart = match.getSecondHalfStartTime();
        LocalTime newStart = request.getStartTime();
        LocalTime newEnd = request.getEndTime();
        LocalTime firstHalfEnd = match.getFirstHalfEndTime();

        if (newStart != null && currentSecondHalfEnd != null && newStart.isAfter(currentSecondHalfEnd)) {
            throw new ApiException(MatchStatus.TIME_ORDER_INVALID);
        }

        if (newEnd != null && currentSecondHalfStart != null && newEnd.isBefore(currentSecondHalfStart)) {
            throw new ApiException(MatchStatus.INVALID_TIME_RANGE);
        }

        if (newStart != null && firstHalfEnd != null && newStart.isBefore(firstHalfEnd)) {
            throw new ApiException(MatchStatus.SECOND_HALF_TIME_ERROR);
        }
    }
}
