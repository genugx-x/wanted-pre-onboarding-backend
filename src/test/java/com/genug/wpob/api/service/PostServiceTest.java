package com.genug.wpob.api.service;

import com.genug.wpob.api.domain.Post;
import com.genug.wpob.api.domain.User;
import com.genug.wpob.api.repository.PostRepository;
import com.genug.wpob.api.repository.UserRepository;
import com.genug.wpob.api.request.PostCreate;
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
    @DisplayName("게시글 1개 생성 테스트 - 성공")
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
}