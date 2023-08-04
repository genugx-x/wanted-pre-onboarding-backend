package com.genug.wpob.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genug.wpob.api.repository.UserRepository;
import com.genug.wpob.api.request.SignUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 테스트 정상 - (이메일 '@'포함, 비밀번호 8자리)")
    void signUpSuccessTest() throws Exception {
        // given
        String email = "@";
        String password = "12341234";
        SignUp signUp = SignUp.builder()
                .email(email)
                .password(password)
                .build();
        String json = objectMapper.writeValueAsString(signUp);

        // expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 시 email에 '@'가 포함되지 않은 경우 실패")
    void signUpFailTest1() throws Exception {
        // given
        String email = "a";
        String password = "12341234";
        SignUp signUp = SignUp.builder()
                .email(email)
                .password(password)
                .build();
        String json = objectMapper.writeValueAsString(signUp);

        // expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 시 password가 8자 미만이면 실패")
    void signUpFailTest2() throws Exception {
        // given
        String email = "@";
        String password = "1234567";
        SignUp signUp = SignUp.builder()
                .email(email)
                .password(password)
                .build();
        String json = objectMapper.writeValueAsString(signUp);

        // expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 시 이메일 공백 요청시 실패")
    void signUpFailTest3() throws Exception {
        // given
        String email = "";
        String password = "1234567";
        SignUp signUp = SignUp.builder()
                .email(email)
                .password(password)
                .build();
        String json = objectMapper.writeValueAsString(signUp);

        // expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 시 비밀번호 공백 요청시 실패")
    void signUpFailTest4() throws Exception {
        // given
        String email = "@";
        String password = "";
        SignUp signUp = SignUp.builder()
                .email(email)
                .password(password)
                .build();
        String json = objectMapper.writeValueAsString(signUp);

        // expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

}