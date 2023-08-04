package com.genug.wpob.api.service;

import com.genug.wpob.api.repository.UserRepository;
import com.genug.wpob.api.request.SignUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;


    @Autowired
    private UserRepository userRepository;


    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void SignUpPassTest() {
        // given
        String email = "test@test.com";
        String password = "12341234";
        SignUp signUp = SignUp.builder()
                .email(email)
                .password(password)
                .build();

        // when
        userService.create(signUp);

        // then
        assertEquals(1L, userRepository.count());
    }

}