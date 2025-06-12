package com.matchday.matchdayserver.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matchday.matchdayserver.IntegrationTestSupport;
import com.matchday.matchdayserver.auth.model.dto.enums.JwtTokenType;
import com.matchday.matchdayserver.common.auth.JwtTokenProvider;
import com.matchday.matchdayserver.user.model.dto.LoginUserDto;
import com.matchday.matchdayserver.user.model.dto.request.UpdateUserRoleRequest;
import com.matchday.matchdayserver.user.model.dto.request.UserCreateRequest;
import com.matchday.matchdayserver.user.model.dto.request.UserJoinTeamRequest;
import com.matchday.matchdayserver.user.model.enums.Role;
import com.matchday.matchdayserver.user.model.enums.SocialType;
import com.matchday.matchdayserver.user.model.entity.User;
import com.matchday.matchdayserver.user.repository.UserRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest extends IntegrationTestSupport {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired UserRepository userRepository;
    @Autowired JwtTokenProvider jwtTokenProvider;

    private String adminToken;
    private String superAdminToken;
    private String userToken;
    private Long userId;

    @BeforeEach
    void setup() {
        User user = userRepository.save(User.builder().name("홍길동").build());
        userId = user.getId();

        adminToken = jwtTokenProvider.createToken(
            LoginUserDto.builder().id(100L).email("admin@test.com").role(Role.ADMIN).socialType(SocialType.GOOGLE).build(),
            JwtTokenType.ACCESS
        );

        superAdminToken = jwtTokenProvider.createToken(
            LoginUserDto.builder().id(200L).email("super@test.com").role(Role.SUPER_ADMIN).socialType(SocialType.KAKAO).build(),
            JwtTokenType.ACCESS
        );

        userToken = jwtTokenProvider.createToken(
            LoginUserDto.builder().id(userId).email("user@test.com").role(Role.USER).socialType(SocialType.GOOGLE).build(),
            JwtTokenType.ACCESS
        );
    }

    @Test
    @DisplayName("유저 생성 - 성공")
    void createUser_success() throws Exception {
        UserCreateRequest request = new UserCreateRequest("테스트유저", null);

        mockMvc.perform(post("/api/v1/users")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("팀 입단 - 성공 (JWT 필요)")
    void joinTeam_success() throws Exception {
        UserJoinTeamRequest request = UserJoinTeamRequest.builder().teamId(1L).number(10).defaultPosition("FW").build();

        mockMvc.perform(post("/api/v1/users/{userId}/teams", userId)
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }

//    @Test
//    @DisplayName("업로드 presigned URL 발급 - 성공")
//    void uploadUrl_success() throws Exception {
//        mockMvc.perform(get("/api/v1/users/{userId}/profile/upload-url", userId)
//                .param("extension", "jpg"))
//            .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("조회 presigned URL 발급 - 성공")
//    void readUrl_success() throws Exception {
//        mockMvc.perform(get("/api/v1/users/{userId}/profile/read-url", userId)
//                .param("key", "users/1/example.jpg"))
//            .andExpect(status().isOk());
//    }

    @Test
    @DisplayName("유저 정보 조회 - 성공 (ADMIN)")
    void getUserInfo_success() throws Exception {
        mockMvc.perform(get("/api/v1/users/{userId}", userId)
                .header("Authorization", "Bearer " + adminToken))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그아웃 - 성공 (JWT 필요)")
    void logout_success() throws Exception {
        mockMvc.perform(post("/api/v1/users/logout")
                .header("Authorization", "Bearer " + userToken))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("권한 변경 - 성공 (SUPER_ADMIN)")
    void updateRole_success() throws Exception {
        UpdateUserRoleRequest request = new UpdateUserRoleRequest(userId, Role.ADMIN);

        mockMvc.perform(post("/api/v1/users/roles")
                .header("Authorization", "Bearer " + superAdminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.updatedRole").value("ADMIN"));
    }

    @Test
    @DisplayName("권한 변경 - 실패 (ADMIN이 요청할 경우)")
    void updateRole_forbidden() throws Exception {
        UpdateUserRoleRequest request = new UpdateUserRoleRequest(userId, Role.ADMIN);

        mockMvc.perform(post("/api/v1/users/roles")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isForbidden());
    }
}