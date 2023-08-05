package com.genug.wpob.api.service;

import com.genug.wpob.api.domain.User;
import com.genug.wpob.api.repository.UserRepository;
import com.genug.wpob.api.request.Signup;
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
    @DisplayName("회원가입에 성공 테스트 및 비밀번호 암호화 체크")
    void SignUpPassTest() {
        // given
        String email = "test@test.com";
        String password = "12341234";
        Signup signUp = Signup.builder()
                .email(email)
                .password(password)
                .build();

        // when
        User user = userService.create(signUp);

        // then
        assertEquals(1L, userRepository.count());
        assertNotEquals(password, user.getPassword());
    }

    @Test
    @DisplayName("로그인 테스트 - 성공")
    void loginSuccessTest() {
        // given
        String email = "test@test.com";
        String password = "12341234";
        User user = userService.create(Signup.builder()
                .email(email)
                .password(password)
                .build());

        // when
        User loginUser = userService.verifyEmailAndPassword(email, password);

        // then
        assertNotNull(loginUser);
    }

    @Test
    @DisplayName("로그인 테스트 - 실패: 계정이 없는 경우 예외 발생")
    void loginFailTest1() {
        // given
        String email = "test@test.com";
        String password = "12341234";
        User user = userService.create(Signup.builder()
                .email(email)
                .password(password)
                .build());

        // then
        assertThrows(RuntimeException.class, () ->
                userService.verifyEmailAndPassword("none-user", password));
    }

    @Test
    @DisplayName("로그인 테스트 - 실패: 비밀번호가 틀린 경우")
    void loginFailTest2() {
        // given
        String email = "test@test.com";
        String password = "12341234";
        User user = userService.create(Signup.builder()
                .email(email)
                .password(password)
                .build());

        // then
        assertThrows(RuntimeException.class, () ->
                userService.verifyEmailAndPassword(email, "wrong-password"));
    }

}