package com.genug.wpob.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genug.wpob.api.domain.User;
import com.genug.wpob.api.repository.PostRepository;
import com.genug.wpob.api.repository.UserRepository;
import com.genug.wpob.api.request.Login;
import com.genug.wpob.api.request.PostCreate;
import com.genug.wpob.api.response.LoginResponse;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        String email = "post@test.com";
        String password = "pOStTEstCom";
        userRepository.save(User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .build());
    }

    @AfterEach
    void cleanUp() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 생성 - 성공")
    void postCreateTest() throws Exception {
        // given
        String email = "post@test.com";
        String password = "pOStTEstCom";
        Login login = Login.builder()
                .email(email)
                .password(password)
                .build();
        String loginJson = objectMapper.writeValueAsString(login);

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andReturn();
        LoginResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), LoginResponse.class);
        String authentication = "Bearer " + response.getToken();

        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        String postCreateJson = objectMapper.writeValueAsString(postCreate);

        // expected
        mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authentication", authentication)
                .content(postCreateJson))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertEquals(1L, postRepository.count());
    }
}