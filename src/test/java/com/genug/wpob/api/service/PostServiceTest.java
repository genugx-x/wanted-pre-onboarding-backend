package com.genug.wpob.api.service;

import com.genug.wpob.api.domain.User;
import com.genug.wpob.api.repository.PostRepository;
import com.genug.wpob.api.repository.UserRepository;
import com.genug.wpob.api.request.PostCreate;
import com.genug.wpob.api.response.PostsResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.TestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostService postService;

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
    void clearUp() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 1개 생성 테스트")
    void postCreateTest() {
        // given
        String email = "post@test.com";
        User user = userRepository.findByEmail(email).orElseThrow(TestAbortedException::new);

        String title = "제목입니다.";
        String content = "내용입니다.";
        PostCreate postCreate = PostCreate.builder()
                .title(title)
                .content(content)
                .build();

        // when
        postService.create(user.getId(), postCreate);

        // then
        assertEquals(1L, postRepository.count());
    }

    @Test
    @DisplayName("게시글 목록 조회 테스트: page=4, size=10인 경우 61~70번 글 조회")
    void postGetListTest1() {
        // given
        String email = "post@test.com";
        User user = userRepository.findByEmail(email).orElseThrow(TestAbortedException::new);
        for (int i = 1; i <= 100; i++) {
            String title = "title=" + i;
            String content = "content=" + i;
            PostCreate postCreate = PostCreate.builder()
                    .title(title)
                    .content(content)
                    .build();
            postService.create(user.getId(), postCreate);
        }

        // when
        final int size = 10;
        final int page = 4;
        PostsResponse response = postService.getList(size, page);

        // then
        assertEquals(10L, response.getPosts().size());
        assertEquals(100, response.getTotalPostCount());
        assertEquals(10, response.getTotalPageCount());
        assertEquals("title=70", response.getPosts().get(0).getTitle());
        assertEquals("title=61", response.getPosts().get(9).getTitle());
    }

    @Test
    @DisplayName("게시글 목록 조회 테스트: 페이지에 표출되는 목록 수(size) 보다 게시글이 작은 경우 게시글 수만큼만 조회")
    void postGetListTest2() {
        // given
        String email = "post@test.com";
        User user = userRepository.findByEmail(email).orElseThrow(TestAbortedException::new);
        for (int i = 1; i <= 9; i++) {
            String title = "title=" + i;
            String content = "content=" + i;
            PostCreate postCreate = PostCreate.builder()
                    .title(title)
                    .content(content)
                    .build();
            postService.create(user.getId(), postCreate);
        }

        // when
        final int size = 10;
        final int page = 1;
        PostsResponse response = postService.getList(size, page);

        // then
        assertEquals(9L, response.getPosts().size());
        assertEquals(9L, response.getTotalPostCount());
        assertEquals(1, response.getTotalPageCount());
        assertEquals("title=9", response.getPosts().get(0).getTitle());
        assertEquals("title=1", response.getPosts().get(8).getTitle());
    }
}