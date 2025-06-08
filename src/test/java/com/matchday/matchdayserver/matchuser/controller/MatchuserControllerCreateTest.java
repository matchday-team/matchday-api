package com.matchday.matchdayserver.matchuser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matchday.matchdayserver.IntegrationTestSupport;
import com.matchday.matchdayserver.auth.model.dto.enums.JwtTokenType;
import com.matchday.matchdayserver.common.auth.JwtTokenProvider;
import com.matchday.matchdayserver.match.model.dto.request.MatchCreateRequest;
import com.matchday.matchdayserver.match.model.entity.Match.MatchType;
import com.matchday.matchdayserver.match.service.MatchCreateService;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserCreateRequest;
import com.matchday.matchdayserver.matchuser.model.enums.MatchUserRole;
import com.matchday.matchdayserver.team.model.entity.Team;
import com.matchday.matchdayserver.team.repository.TeamRepository;
import com.matchday.matchdayserver.user.model.dto.LoginUserDto;
import com.matchday.matchdayserver.user.model.enums.Role;
import com.matchday.matchdayserver.user.model.enums.SocialType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MatchUserControllerCreateTest extends IntegrationTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MatchCreateService matchCreateService;

    @Autowired
    private TeamRepository teamRepository;

    private Long matchId;
    private String accessToken;

    @BeforeEach
    void setUp() {
        // given: 매치 생성
        MatchCreateRequest request = new MatchCreateRequest(
            "테스트 경기",
            1L, 2L,
            MatchType.리그,
            "서울월드컵경기장",
            LocalDate.of(2025, 6, 10),
            LocalTime.of(14, 0),
            LocalTime.of(16, 0),
            45,
            45,
            "김주심",
            "이부심",
            "박부심",
            "정대기"
        );
        matchId = matchCreateService.create(request);

        // given: ADMIN 사용자로 토큰 발급
        LoginUserDto loginUser = LoginUserDto.builder()
            .id(1L)
            .email("admin@example.com")
            .role(Role.ADMIN)
            .socialType(SocialType.GOOGLE)
            .build();
        accessToken = jwtTokenProvider.createToken(loginUser, JwtTokenType.ACCESS);

    }

    private MatchUserCreateRequest createRequest(Long userId, Long teamId) {
        return new MatchUserCreateRequest(
            userId,
            teamId,
            MatchUserRole.START_PLAYER,
            "FW",
            1
        );
    }

    @Test
    @DisplayName("매치 유저 생성 - 성공(Role : ADMIN)")
    void createMatchUser_Success_Admin() throws Exception {
        // given
        MatchUserCreateRequest request = createRequest(1L,1L);

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/matches/{matchId}/users", matchId)
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        // then
        actions
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("매치 유저 생성 - 성공(Role : SUPER_ADMIN)")
    void createMatchUser_Success_Super_Admin() throws Exception {
        // given: 일반 Super Admin 권한 유저로 토큰 생성
        LoginUserDto user = LoginUserDto.builder()
            .id(2L)
            .email("user@example.com")
            .role(Role.SUPER_ADMIN)
            .socialType(SocialType.GOOGLE)
            .build();
        String userToken = jwtTokenProvider.createToken(user, JwtTokenType.ACCESS);

        MatchUserCreateRequest request = createRequest(user.getId(), 1L);

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/matches/{matchId}/users", matchId)
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        // then
        actions
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("매치 유저 생성 - 실패 (권한 없는 사용자(USER)")
    void createMatchUser_Fail_UserRoleNotAllowed() throws Exception {
        // given: 일반 USER 권한 유저로 토큰 생성
        LoginUserDto user = LoginUserDto.builder()
            .id(2L)
            .email("user@example.com")
            .role(Role.USER)
            .socialType(SocialType.GOOGLE)
            .build();
        String userToken = jwtTokenProvider.createToken(user, JwtTokenType.ACCESS);

        MatchUserCreateRequest request = createRequest(user.getId(), 1L);

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/matches/{matchId}/users", matchId)
            .header("Authorization", "Bearer " + userToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        // then
        actions
            .andDo(print())
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("매치 유저 생성 - 실패 (인증되지 않은 사용자)")
    void createMatchUser_Fail_Unauthenticated() throws Exception {
        MatchUserCreateRequest request = createRequest(2L, 1L);

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/matches/{matchId}/users", matchId)
            // 토큰 헤더 없이 요청
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        // then
        actions
            .andDo(print())
            .andExpect(status().isUnauthorized()); // 401
    }


    @Test
    @DisplayName("매치 유저 생성 - 실패 (존재하지 않는 매치)")
    void createMatchUser_Fail_MatchNotFound() throws Exception {
        // given
        MatchUserCreateRequest request = createRequest(1L,1L);

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/matches/{matchId}/users", 999999L)
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        // then
        actions
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("매치 유저 생성 - 실패 (존재하지 않는 유저)")
    void createMatchUser_Fail_UserNotFound() throws Exception {
        // given: 존재하지 않는 유저로 토큰 발급 및 요청 생성
        LoginUserDto fakeUser = LoginUserDto.builder()
            .id(9999L)
            .email("notfound@example.com")
            .role(Role.ADMIN)
            .socialType(SocialType.GOOGLE)
            .build();

        String fakeAccessToken = jwtTokenProvider.createToken(fakeUser, JwtTokenType.ACCESS);
        MatchUserCreateRequest request = createRequest(fakeUser.getId(), 1L);

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/matches/{matchId}/users", matchId)
            .header("Authorization", "Bearer " + fakeAccessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        // then
        actions
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("매치 유저 생성 - 성공 (팀 없이 ADMIN 역할)")
    void createMatchUser_Success_TeamNull_AdminRole() throws Exception {
        // given: 팀 ID 없이 ADMIN 역할 요청
        MatchUserCreateRequest request = new MatchUserCreateRequest(
            1L,
            null,
            MatchUserRole.ADMIN,
            null,
            null
        );

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/matches/{matchId}/users", matchId)
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        // then
        actions
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("매치 유저 생성 - 성공 (팀 없이 ARCHIVES 역할)")
    void createMatchUser_Success_TeamNull_ArchivesRole() throws Exception {
        // given: 팀 ID 없이 ARCHIVES 역할 요청
        MatchUserCreateRequest request = new MatchUserCreateRequest(
            1L,
            null,
            MatchUserRole.ARCHIVES,
            null,
            null
        );

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/matches/{matchId}/users", matchId)
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        // then
        actions
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("매치 유저 생성 - 실패 (매치를 등록하지 않은 팀)")
    void createMatchUser_Fail_TeamNotInMatch() throws Exception {
        // given: 매치에 포함되지 않은 새로운 팀 생성 및 저장
        Team otherTeam = new Team("테스트팀", "#FFFFFF", "#FFFFFF", "#FFFFFF", null);
        teamRepository.save(otherTeam);

        MatchUserCreateRequest request = createRequest(1L, otherTeam.getId());

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/matches/{matchId}/users", matchId)
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        // then
        actions
            .andDo(print())
            .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("매치 유저 생성 - 실패 (중복 등록)")
    void createMatchUser_Fail_Duplicate() throws Exception {
        // given: 이미 등록된 매치 유저가 있다고 가정하고 동일 요청 반복
        MatchUserCreateRequest request = createRequest(1L,1L);

        // 최초 등록 (성공)
        mockMvc.perform(post("/api/v1/matches/{matchId}/users", matchId)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());

        // when: 동일 요청으로 재등록 시도
        ResultActions actions = mockMvc.perform(post("/api/v1/matches/{matchId}/users", matchId)
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        // then
        actions
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("매치 유저 생성 - 실패 (존재하지 않는 팀)")
    void createMatchUser_Fail_TeamNotFound() throws Exception {
        // given
        long nonExistentTeamId = 9999L;
        MatchUserCreateRequest request = createRequest(1L, nonExistentTeamId);

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/matches/{matchId}/users", matchId)
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        // then
        actions
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("매치 유저 생성 - 실패 (유효하지 않은 MatchUser Role")
    void createMatchUser_Fail_InvalidMatchUserRole() throws Exception{
        // given
        String invalidJson = """
        {
            "userId": 1,
            "teamId": 1,
            "role": "INVALID_ROLE",
            "matchPosition": "FW",
            "matchGrid": 10
        }
        """;

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/matches/{matchId}/users", matchId)
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidJson));

        // then
        actions
            .andDo(print())
            .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("매치 유저 생성 - 실패 (matchUser Role null일 경우")
    void createMatchUser_Fail_MatchUserRoleISNull() throws Exception{
        // given
        String invalidJson = """
        {
            "userId": 1,
            "teamId": 1,
            "matchPosition": "FW",
            "matchGrid": 10
        }
        """;

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/matches/{matchId}/users", matchId)
            .header("Authorization", "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidJson));

        // then
        actions
            .andDo(print())
            .andExpect(status().isBadRequest());
    }
}
