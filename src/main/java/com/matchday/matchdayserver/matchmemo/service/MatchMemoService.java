package com.matchday.matchdayserver.matchmemo.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.MatchStatus;
import com.matchday.matchdayserver.common.response.TeamStatus;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.match.repository.MatchRepository;
import com.matchday.matchdayserver.matchmemo.model.dto.MatchMemoRequest;
import com.matchday.matchdayserver.matchmemo.model.dto.MatchMemoResponse;
import com.matchday.matchdayserver.matchmemo.model.entity.MatchMemo;
import com.matchday.matchdayserver.matchmemo.repository.MatchMemoRepository;
import com.matchday.matchdayserver.team.model.entity.Team;
import com.matchday.matchdayserver.team.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchMemoService {

  private final MatchMemoRepository memoRepository;
  private final MatchRepository matchRepository;
  private final TeamRepository teamRepository;

  @Transactional
  public void createOrUpdate(Long matchId, Long teamId, MatchMemoRequest request) {
    Match match = matchRepository.findById(matchId)
        .orElseThrow(() -> new ApiException(MatchStatus.NOTFOUND_MATCH));
    Team team = teamRepository.findById(teamId)
        .orElseThrow(() -> new ApiException(TeamStatus.NOTFOUND_TEAM));

    MatchMemo memo = memoRepository.findByMatchIdAndTeamId(matchId, teamId)
        .map(existing -> {
          existing.updateMemo(request.getMemo());
          return existing;
        })
        .orElse(MatchMemo.builder()
            .match(match)
            .team(team)
            .memo(request.getMemo())
            .build());

    memoRepository.save(memo);
  }

  public MatchMemoResponse get(Long matchId, Long teamId) {
    MatchMemo memo = memoRepository.findByMatchIdAndTeamId(matchId, teamId)
        .orElseThrow(() -> new ApiException(MatchStatus.MEMO_NOT_FOUND));
    return MatchMemoResponse.builder()
        .matchId(memo.getMatch().getId())
        .teamId(memo.getTeam().getId())
        .memo(memo.getMemo())
        .build();
  }

  public void delete(Long matchId, Long teamId) {
    memoRepository.deleteByMatchIdAndTeamId(matchId, teamId);
  }
}