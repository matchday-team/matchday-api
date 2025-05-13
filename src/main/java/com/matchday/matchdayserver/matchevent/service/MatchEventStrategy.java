package com.matchday.matchdayserver.matchevent.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.matchday.matchdayserver.matchuser.model.entity.MatchUser;
import org.springframework.stereotype.Component;

import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.matchevent.mapper.MatchEventMapper;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventRequest;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventResponse;
import com.matchday.matchdayserver.matchevent.model.entity.MatchEvent;
import com.matchday.matchdayserver.matchevent.model.enums.MatchEventType;
import com.matchday.matchdayserver.matchevent.repository.MatchEventRepository;

import lombok.RequiredArgsConstructor;

/**
 * MatchEventStrategy
 * <p>
 * 경기 이벤트 생성 전략 클래스입니다.
 * <ul>
 * <li>골 기록 시: 유효슛, 슛 이벤트를 함께 생성</li>
 * <li>오프사이드 기록 시: 오프사이드, 파울 이벤트를 함께 생성</li>
 * <li>유효슛 기록 시: 유효슛, 슛 이벤트를 함께 생성</li>
 * <li>경고(옐로 카드, 레드 카드): 파울</li>
 * </ul>
 * <p>
 * 트랜잭션 처리는 상위 서비스에서 담당해야 합니다.
 */
@Component
@RequiredArgsConstructor
public class MatchEventStrategy {

    private final MatchEventRepository matchEventRepository;

    public List<MatchEventResponse> generateMatchEventLog(MatchEventRequest request, Match match,
        MatchUser matchUser) {
        return generateMatchEvent(request, match, matchUser);
    }

    private List<MatchEventResponse> generateMatchEvent(MatchEventRequest request, Match match,
        MatchUser matchUser) {
        switch (request.getEventType()) {
            case GOAL:
                return generateGoalEvent(request, match, matchUser);
            case OFFSIDE:
                return generateOffsideEvent(request, match, matchUser);
            case YELLOW_CARD:
            case RED_CARD:
                return generateCardEvent(request, match, matchUser);
            case VALID_SHOT:
                return generateValidShotEvent(request, match, matchUser);
            default:
                return generateDefaultEvent(request, match, matchUser);
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
    private List<MatchEventResponse> generateGoalEvent(MatchEventRequest request, Match match,
        MatchUser user) {
        MatchEvent goalEvent = MatchEventMapper.toEntity(request, match, user);
        MatchEvent shotEvent = goalEvent.copyWith(MatchEventType.SHOT);
        MatchEvent validShotEvent = goalEvent.copyWith(MatchEventType.VALID_SHOT);
        matchEventRepository.saveAll(List.of(goalEvent, shotEvent, validShotEvent));

        List<MatchEventResponse> matchEventResponses = new ArrayList<>();
        matchEventResponses.add(MatchEventMapper.toResponse(goalEvent));
        matchEventResponses.add(MatchEventMapper.toResponse(shotEvent));
        matchEventResponses.add(MatchEventMapper.toResponse(validShotEvent));

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
     * @param request   오프사이드 이벤트 요청 정보
     * @param match     해당 경기 정보
     * @param matchUser 이벤트를 기록한 사용자
     * @return 생성된 이벤트들의 응답 리스트 (오프사이드, 파울)
     */
    private List<MatchEventResponse> generateOffsideEvent(MatchEventRequest request, Match match,
        MatchUser matchUser) {
        MatchEvent offsideEvent = MatchEventMapper.toEntity(request, match, matchUser);
        MatchEvent foulEvent = offsideEvent.copyWith(MatchEventType.FOUL);
        matchEventRepository.saveAll(List.of(offsideEvent, foulEvent));
        return List.of(
            MatchEventMapper.toResponse(offsideEvent),
            MatchEventMapper.toResponse(foulEvent));
    }

    /**
     * 카드(옐로카드/레드카드) 이벤트 기록 시 호출됩니다.
     * <ul>
     * <li>카드 이벤트(cardEvent)를 생성합니다.</li>
     * <li>만약 YELLOW_CARD이고 이미 YELLOW_CARD가 존재하면 RED_CARD 이벤트도 추가로 생성합니다.</li>
     * <li>경고(WARNING), 파울(FOUL) 이벤트를 각각 복사 생성합니다.</li>
     * <li>생성된 모든 이벤트를 저장하고, 각각의 응답 객체를 반환합니다.</li>
     * </ul>
     *
     * @param request   카드 이벤트 요청 정보
     * @param match     해당 경기 정보
     * @param matchUser 이벤트를 기록한 사용자
     * @return 생성된 이벤트들의 응답 리스트 (카드, 경고, 파울, 필요시 레드카드)
     */
    private List<MatchEventResponse> generateCardEvent(MatchEventRequest request, Match match,
        MatchUser matchUser) {
        MatchEvent cardEvent = MatchEventMapper.toEntity(request, match, matchUser);
        List<MatchEvent> needToSaveEvents = new ArrayList<>();

        if(request.getEventType().equals(MatchEventType.YELLOW_CARD)) {
            boolean isAlreadyGetYellow = matchEventRepository.existsByMatchUserIdAndEventType(
                matchUser.getId(), MatchEventType.YELLOW_CARD);
            if(isAlreadyGetYellow)
                needToSaveEvents.add(cardEvent.copyWith(MatchEventType.RED_CARD));
        }
        needToSaveEvents.add(cardEvent);
        needToSaveEvents.add(cardEvent.copyWith(MatchEventType.WARNING));
        needToSaveEvents.add(cardEvent.copyWith(MatchEventType.FOUL));

        matchEventRepository.saveAll(needToSaveEvents);

        return needToSaveEvents.stream().map(MatchEventMapper::toResponse)
            .collect(Collectors.toList());
    }

    /**
     * 유효슛 이벤트 기록 시 호출됩니다.
     * <ul>
     * <li>유효슛 이벤트(validShotEvent)를 생성합니다.</li>
     * <li>유효슛 이벤트를 기반으로 슛(shotEvent) 이벤트를 복사 생성합니다.</li>
     * <li>두 이벤트를 모두 저장하고, 각각의 응답 객체를 반환합니다.</li>
     * </ul>
     *
     * @param request   유효슛 이벤트 요청 정보
     * @param match     해당 경기 정보
     * @param matchUser 이벤트를 기록한 사용자
     * @return 생성된 이벤트들의 응답 리스트 (유효슛, 슛)
     */
    private List<MatchEventResponse> generateValidShotEvent(MatchEventRequest request, Match match,
        MatchUser matchUser) {
        MatchEvent validShotEvent = MatchEventMapper.toEntity(request, match, matchUser);
        MatchEvent shotEvent = validShotEvent.copyWith(MatchEventType.SHOT);
        matchEventRepository.saveAll(List.of(validShotEvent, shotEvent));
        return List.of(
            MatchEventMapper.toResponse(validShotEvent),
            MatchEventMapper.toResponse(shotEvent));
    }

    /**
     * 기타 이벤트(기본 이벤트) 기록 시 호출됩니다.
     * <ul>
     * <li>요청에 따라 단일 MatchEvent를 생성 및 저장합니다.</li>
     * <li>이벤트에 대한 응답 객체를 반환합니다.</li>
     * </ul>
     *
     * @param request   기타 이벤트 요청 정보
     * @param match     해당 경기 정보
     * @param matchUser 이벤트를 기록한 사용자
     * @return 생성된 이벤트의 응답 리스트 (단일 이벤트)
     */
    private List<MatchEventResponse> generateDefaultEvent(MatchEventRequest request, Match match,
        MatchUser matchUser) {
        MatchEvent matchEvent = MatchEventMapper.toEntity(request, match, matchUser);
        matchEventRepository.save(matchEvent);
        return List.of(MatchEventMapper.toResponse(matchEvent));
    }
}