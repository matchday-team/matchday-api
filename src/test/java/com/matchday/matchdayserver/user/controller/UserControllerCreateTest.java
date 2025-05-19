package com.matchday.matchdayserver.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matchday.matchdayserver.user.model.dto.request.UserCreateRequest;
import com.matchday.matchdayserver.user.model.entity.User;
import com.matchday.matchdayserver.user.repository.UserRepository;

@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerCreateTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("임시 유저 생성 - 정상 케이스")
    void createUser_Success() throws Exception {
        // given
        UserCreateRequest request = new UserCreateRequest("테스트유저", null);

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data").isNumber())
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    @DisplayName("임시 유저 생성 - 이름이 null인 경우")
    void createUser_Fail_NameIsNull() throws Exception {
        // given
        UserCreateRequest request = new UserCreateRequest();
        // name을 설정하지 않음 (null)

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        actions
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("임시 유저 생성 - 중복된 이름인 경우도 생성됨")
    void createUser_Success_DuplicateName() throws Exception {
        // given
        String duplicateName = "중복이름";

        // 먼저 동일한 이름으로 유저 생성
        User existingUser = new User(duplicateName, null);
        userRepository.save(existingUser);

        UserCreateRequest request = new UserCreateRequest(duplicateName, null);

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201)); // 중복 이름도 허용되어 성공적으로 생성됨

    }

    @Test
    @DisplayName("임시 유저 생성 - 프로필 이미지 포함")
    void createUser_WithProfileImg() throws Exception {
        // given
        UserCreateRequest request = new UserCreateRequest("프로필이미지유저", "users/1/test-image.png");

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data").isNumber());
        
        // 생성된 사용자의 프로필 이미지 확인
        Long userId = objectMapper.readTree(
                actions.andReturn().getResponse().getContentAsString())
                .get("data").asLong();
        
        User createdUser = userRepository.findById(userId).orElseThrow();
        assert createdUser.getProfileImg().equals("users/1/test-image.png");
    }
    
    @Test
    @DisplayName("임시 유저 생성 - 이름이 공백인 경우")
    void createUser_Fail_EmptyName() throws Exception {
        // given
        UserCreateRequest request = new UserCreateRequest("", null);

        // when
        ResultActions actions = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        actions
                .andExpect(status().isBadRequest());
    }
}