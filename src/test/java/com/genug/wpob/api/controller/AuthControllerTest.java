package com.genug.wpob.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genug.wpob.api.domain.User;
import com.genug.wpob.api.repository.UserRepository;
import com.genug.wpob.api.request.Login;
import com.genug.wpob.api.request.Signup;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        String email = "json@web.token";
        String encryptedPassword = passwordEncoder.encode("jsoNweBtoKen");
        userRepository.save(User.builder()
                .email("json@web.token")
                .password(encryptedPassword)
                .build());
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 테스트 - 성공: 이메일 '@'포함, 비밀번호 8자리")
    void signUpSuccessTest() throws Exception {
        // given
        String email = "@";
        String password = "12341234";
        Signup signUp = Signup.builder()
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
    @DisplayName("회원가입 테스트 - 실패: email에 '@'가 포함되지 않은 경우 실패")
    void signUpFailTest1() throws Exception {
        // given
        String email = "a";
        String password = "12341234";
        Signup signUp = Signup.builder()
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
    @DisplayName("회원가입 테스트 - 실패: password가 8자 미만이면 실패")
    void signUpFailTest2() throws Exception {
        // given
        String email = "@";
        String password = "1234567";
        Signup signUp = Signup.builder()
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
    @DisplayName("회원가입 테스트 - 실패: 이메일 공백 요청시 실패")
    void signUpFailTest3() throws Exception {
        // given
        String email = "";
        String password = "1234567";
        Signup signUp = Signup.builder()
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
    @DisplayName("회원가입 테스트 - 실패: 비밀번호 공백 요청시 실패")
    void signUpFailTest4() throws Exception {
        // given
        String email = "@";
        String password = "";
        Signup signUp = Signup.builder()
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
    @DisplayName("로그인 테스트 - 성공: 결과값으로 jwt 반환")
    void loginSuccessTest() throws Exception {
        // given
        String email = "json@web.token";
        String password = "jsoNweBtoKen";
        Login login = Login.builder()
                .email(email)
                .password(password)
                .build();
        String json = objectMapper.writeValueAsString(login);

        // expected
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.token").exists()
                )
                .andDo(print())
                .andReturn();
    }

}