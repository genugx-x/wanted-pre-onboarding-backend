package com.genug.wpob.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genug.wpob.api.domain.Post;
import com.genug.wpob.api.domain.User;
import com.genug.wpob.api.repository.PostRepository;
import com.genug.wpob.api.repository.UserRepository;
import com.genug.wpob.api.request.Login;
import com.genug.wpob.api.request.PostCreate;
import com.genug.wpob.api.response.LoginResponse;
import com.genug.wpob.api.response.PostsResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    String login() throws Exception {
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
        return response.getToken();
    }

    void createPosts(int count) {
        String email = "post@test.com";
        User user = userRepository.findByEmail(email).orElseThrow(TestAbortedException::new);
        for (int i = 1; i <= count; i++) {
            postRepository.save(Post.builder()
                    .user(user)
                    .title("title=" + i)
                    .content("content=" + i)
                    .build());
        }
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

    @Test
    @DisplayName("게시글 목록 조회: 전체글 100개, size=10, page=2 조회")
    void postGetListTest() throws Exception {
        // given
        String token = login();
        createPosts(100);

        // expected
        mockMvc.perform(get("/posts?size=10&page=2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authentication", "Bearer " + token))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.totalPostCount").value(100),
                        jsonPath("$.totalPageCount").value(10),
                        jsonPath("$.posts.length()", is(10)))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 목록 조회: 전체 글 수 < size 인 경우")
    void postGetListTest2() throws Exception {
        // given
        String token = login();
        createPosts(6);

        // expected
        mockMvc.perform(get("/posts?size=10&page=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authentication", "Bearer " + token))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.totalPostCount").value(6),
                        jsonPath("$.totalPageCount").value(1),
                        jsonPath("$.posts.length()", is(6)))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 목록 조회: size, page 요청 없는 경우 default size=10, page=1로 조회")
    void postGetListTest3() throws Exception {
        // given
        String token = login();
        createPosts(100);

        // expected
        mockMvc.perform(get("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authentication", "Bearer " + token))
                .andExpectAll(
                        status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("특정 게시글 조회 - 정상")
    void postGetSuccessTest() throws Exception {
        // given
        String token = login();
        createPosts(20);
        List<Post> posts = postRepository.getList(10, 1);
        long postId = posts.get(0).getId();

        // expected
        mockMvc.perform(get("/posts/{postId}", postId)
                        .header("Authentication", "Bearer " + token))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(postId),
                        jsonPath("$.title").value("title=20"),
                        jsonPath("$.content").value("content=20"))
                .andDo(print());
    }

    @Test
    @DisplayName("특정 게시글 조회 - 존재 하지 않는 postId 요청 시 예외 반환")
    void postGetFailTest() throws Exception {
        // given
        String token = login();
        createPosts(5);
        List<Post> posts = postRepository.getList(10, 1);
        long postId = 9999;

        // expected
        mockMvc.perform(get("/posts/{postId}", postId)
                        .header("Authentication", "Bearer " + token))
                .andExpectAll(
                        status().isNotFound(),
                        jsonPath("$.code").value(404),
                        jsonPath("$.message").value("존재하지 않는 게시글입니다."))
                .andDo(print());
    }
}