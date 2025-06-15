package com.matchday.matchdayserver.matchevent.service;

import org.springframework.stereotype.Service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.AuthStatus;
import com.matchday.matchdayserver.matchuser.repository.MatchUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchEventRecordValidateService {

  private final MatchUserRepository matchUserRepository;

  /**
   * 경기 이벤트 저장 권한이 있는 유저인지 검증합니다.
   *
   * @param matchId 경기 ID
   * 
   */
  public void validateEventSavePermission(Long matchId, Long userId) {
    int count = matchUserRepository.getCountArchiver(matchId, userId);
    if (count == 0) {
      throw new ApiException(AuthStatus.FORBIDDEN);
    }
  }
}
