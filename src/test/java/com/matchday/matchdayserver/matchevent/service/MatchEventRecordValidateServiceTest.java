package com.matchday.matchdayserver.matchevent.service;

import com.matchday.matchdayserver.IntegrationTestSupport;
import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.AuthStatus;
import com.matchday.matchdayserver.match.model.dto.request.MatchCreateRequest;
import com.matchday.matchdayserver.match.model.dto.request.MockMatchCreateRequest;
import com.matchday.matchdayserver.match.service.MatchCreateService;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserCreateRequest;
import com.matchday.matchdayserver.matchuser.model.dto.MockMatchUserCreateRequest;
import com.matchday.matchdayserver.matchuser.service.MatchUserService;
import com.matchday.matchdayserver.team.model.entity.MockTeam;
import com.matchday.matchdayserver.team.model.entity.Team;
import com.matchday.matchdayserver.team.repository.TeamRepository;
import com.matchday.matchdayserver.user.model.entity.MockUser;
import com.matchday.matchdayserver.user.model.entity.User;
import com.matchday.matchdayserver.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class MatchEventRecordValidateServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private MatchCreateService matchCreateService;
    @Autowired
    private MatchUserService matchUserService;


    @Autowired
    private MatchEventRecordValidateService matchEventRecordValidateService;

    private Long homeTeamId;
    private User adminUser;
    private User normalUser;
    private Long matchId;

    @BeforeEach
    public void setUp() {
        // given
        Team homeTeam = MockTeam.create("RokyTeam");
        Team awayTeam = MockTeam.create("LokyTeam");
        teamRepository.save(homeTeam);
        teamRepository.save(awayTeam);

        homeTeamId = homeTeam.getId();

        MatchCreateRequest matchCreateRequest = MockMatchCreateRequest.create(homeTeam.getId(),
            awayTeam.getId());
        matchId = matchCreateService.create(matchCreateRequest);
    }

    @Test
    @DisplayName("관리자 유저는 경기 이벤트 저장 권한이 있다.")
    public void validateEventSavePermission_AdminUser() {
        // given
        adminUser = MockUser.createAdmin("테스트 관리자");
        adminUser = userRepository.save(adminUser);

        // when
        userRepository.save(adminUser);

        // then
        assertDoesNotThrow(() -> matchEventRecordValidateService.validateEventSavePermission(matchId
            , adminUser.getId()));
    }

    @Test
    @DisplayName("일반 유저는 경기 이벤트 저장 권한이 없다.")
    public void validateEventSavePermission_NormalUser() {
        // given
        normalUser = MockUser.create("테스트 일반 유저");
        normalUser = userRepository.save(normalUser);

        // when
        ApiException exception = assertThrows(ApiException.class, () ->
            matchEventRecordValidateService.validateEventSavePermission(matchId, normalUser.getId())
        );

        // then
        assertEquals(AuthStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    @DisplayName("일반 유저가 MatchUser.Archives 권한을 가진 경우 경기 이벤트 저장 권한이 있다.")
    public void validateEventSavePermission_NormalUserWithArchivesRole() {
        // given
        normalUser = MockUser.create("테스트 일반 유저");
        normalUser = userRepository.save(normalUser);
        Long userId = normalUser.getId();
        MatchUserCreateRequest matchUserCreateRequest = MockMatchUserCreateRequest.createArchives(userId, homeTeamId);
        matchUserService.create(matchId, matchUserCreateRequest);

        // when
        userRepository.save(normalUser);

        // then
        assertDoesNotThrow(() -> matchEventRecordValidateService.validateEventSavePermission(matchId
            , normalUser.getId()));
    }

}