package com.genug.wpob.api.service;

import com.genug.wpob.api.domain.User;
import com.genug.wpob.api.exception.LoginFailException;
import com.genug.wpob.api.exception.UserNotFoundException;
import com.genug.wpob.api.repository.UserRepository;
import com.genug.wpob.api.request.Signup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User findById(final Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User create(final Signup signup) {
        if (userRepository.existsByEmail(signup.getEmail())) {
            throw new RuntimeException("이미 가입된 이메일입니다.");
        }
        String encryptedPassword = passwordEncoder.encode(signup.getPassword());
        User user = User.builder()
                .email(signup.getEmail())
                .password(encryptedPassword)
                .build();
        userRepository.save(user);
        return user;
    }

    public User verifyEmailAndPassword(final String email, final String password) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        throw new LoginFailException();
    }
}
