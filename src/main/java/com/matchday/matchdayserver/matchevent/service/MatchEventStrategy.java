package com.matchday.matchdayserver.matchevent.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.TeamStatus;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.matchevent.mapper.MatchEventMapper;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventRequest;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventResponse;
import com.matchday.matchdayserver.matchevent.model.entity.MatchEvent;
import com.matchday.matchdayserver.matchevent.model.enums.MatchEventType;
import com.matchday.matchdayserver.matchevent.repository.MatchEventRepository;
import com.matchday.matchdayserver.team.model.entity.Team;
import com.matchday.matchdayserver.team.repository.TeamRepository;
import com.matchday.matchdayserver.user.model.entity.User;

import lombok.RequiredArgsConstructor;

/**
 * MatchEventStrategy
 * <p>
 * 경기 이벤트 생성 전략 클래스입니다.
 * <ul>
 * <li>골 기록 시: 유효슛, 슛 이벤트를 함께 생성</li>
 * <li>오프사이드 기록 시: 오프사이드, 파울 이벤트를 함께 생성</li>
 * <li>유효슛 기록 시: 유효슛, 슛 이벤트를 함께 생성</li>
 * </ul>
 * <p>
 * 트랜잭션 처리는 상위 서비스에서 담당해야 합니다.
 */
@Component
@RequiredArgsConstructor
public class MatchEventStrategy {

  private final MatchEventRepository matchEventRepository;
  private final TeamRepository teamRepository;

  public List<MatchEventResponse> generateMatchEventLog(MatchEventRequest request, Match match, User user) {
    return generateMatchEvent(request, match, user);
  }

  private List<MatchEventResponse> generateMatchEvent(MatchEventRequest request, Match match, User user) {
    switch (request.getEventType()) {
      case GOAL:
        return generateGoalEvent(request, match, user);
      case OFFSIDE:
        return generateOffsideEvent(request, match, user);
      case VALID_SHOT:
        return generateValidShotEvent(request, match, user);
      default:
        return generateDefaultEvent(request, match, user);
    }
  }

  /**
   * 골 이벤트 기록 시 호출됩니다.
   * <ul>
   * <li>실제 골 이벤트(goalEvent)를 생성합니다.</li>
   * <li>골 이벤트를 기반으로 슛(shotEvent), 유효슛(validShotEvent) 이벤트를 각각 복사 생성합니다.</li>
   * <li>세 이벤트를 모두 저장하고, 각각의 응답 객체를 반환합니다.</li>
   * </ul>
   * 
   * @param request 골 이벤트 요청 정보
   * @param match   해당 경기 정보
   * @param user    이벤트를 기록한 사용자
   * @return 생성된 이벤트들의 응답 리스트 (골, 슛, 유효슛)
   */
  private List<MatchEventResponse> generateGoalEvent(MatchEventRequest request, Match match, User user) {
    MatchEvent goalEvent = MatchEventMapper.toEntity(request, match, user);
    MatchEvent shotEvent = goalEvent.copyWith(MatchEventType.SHOT);
    MatchEvent validShotEvent = goalEvent.copyWith(MatchEventType.VALID_SHOT);
    matchEventRepository.saveAll(List.of(goalEvent, shotEvent, validShotEvent));

    List<MatchEventResponse> matchEventResponses = new ArrayList<>();
    Team team = findByMatchIdAndUserIdOrThrow(match, user);
    matchEventResponses.add(MatchEventMapper.toResponse(goalEvent, team));
    matchEventResponses.add(MatchEventMapper.toResponse(shotEvent, team));
    matchEventResponses.add(MatchEventMapper.toResponse(validShotEvent, team));

    return matchEventResponses;
  }

  /**
   * 오프사이드 이벤트 기록 시 호출됩니다.
   * <ul>
   * <li>오프사이드 이벤트(offsideEvent)를 생성합니다.</li>
   * <li>오프사이드 이벤트를 기반으로 파울(foulEvent) 이벤트를 복사 생성합니다.</li>
   * <li>두 이벤트를 모두 저장하고, 각각의 응답 객체를 반환합니다.</li>
   * </ul>
   * 
   * @param request 오프사이드 이벤트 요청 정보
   * @param match   해당 경기 정보
   * @param user    이벤트를 기록한 사용자
   * @return 생성된 이벤트들의 응답 리스트 (오프사이드, 파울)
   */
  private List<MatchEventResponse> generateOffsideEvent(MatchEventRequest request, Match match, User user) {
    MatchEvent offsideEvent = MatchEventMapper.toEntity(request, match, user);
    MatchEvent foulEvent = offsideEvent.copyWith(MatchEventType.FOUL);
    matchEventRepository.saveAll(List.of(offsideEvent, foulEvent));
    Team team = findByMatchIdAndUserIdOrThrow(match, user);
    return List.of(
        MatchEventMapper.toResponse(offsideEvent, team),
        MatchEventMapper.toResponse(foulEvent, team));
  }

  /**
   * 유효슛 이벤트 기록 시 호출됩니다.
   * <ul>
   * <li>유효슛 이벤트(validShotEvent)를 생성합니다.</li>
   * <li>유효슛 이벤트를 기반으로 슛(shotEvent) 이벤트를 복사 생성합니다.</li>
   * <li>두 이벤트를 모두 저장하고, 각각의 응답 객체를 반환합니다.</li>
   * </ul>
   * 
   * @param request 유효슛 이벤트 요청 정보
   * @param match   해당 경기 정보
   * @param user    이벤트를 기록한 사용자
   * @return 생성된 이벤트들의 응답 리스트 (유효슛, 슛)
   */
  private List<MatchEventResponse> generateValidShotEvent(MatchEventRequest request, Match match, User user) {
    MatchEvent validShotEvent = MatchEventMapper.toEntity(request, match, user);
    MatchEvent shotEvent = validShotEvent.copyWith(MatchEventType.SHOT);
    matchEventRepository.saveAll(List.of(validShotEvent, shotEvent));
    Team team = findByMatchIdAndUserIdOrThrow(match, user);
    return List.of(
        MatchEventMapper.toResponse(validShotEvent, team),
        MatchEventMapper.toResponse(shotEvent, team));
  }

  /**
   * 기타 이벤트(기본 이벤트) 기록 시 호출됩니다.
   * <ul>
   * <li>요청에 따라 단일 MatchEvent를 생성 및 저장합니다.</li>
   * <li>이벤트에 대한 응답 객체를 반환합니다.</li>
   * </ul>
   * 
   * @param request 기타 이벤트 요청 정보
   * @param match   해당 경기 정보
   * @param user    이벤트를 기록한 사용자
   * @return 생성된 이벤트의 응답 리스트 (단일 이벤트)
   */
  private List<MatchEventResponse> generateDefaultEvent(MatchEventRequest request, Match match, User user) {
    MatchEvent matchEvent = MatchEventMapper.toEntity(request, match, user);
    matchEventRepository.save(matchEvent);
    Team team = findByMatchIdAndUserIdOrThrow(match, user);
    return List.of(MatchEventMapper.toResponse(matchEvent, team));
  }

  private Team findByMatchIdAndUserIdOrThrow(Match match, User user) {
    Team team = teamRepository.findByMatchIdAndUserId(match.getId(), user.getId()).orElseThrow(
        () -> new ApiException(TeamStatus.NOTFOUND_TEAM));
    return team;
  }
}