package com.genug.wpob.api.service;

import com.genug.wpob.api.domain.User;
import com.genug.wpob.api.repository.UserRepository;
import com.genug.wpob.api.request.SignUp;
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

    public User create(SignUp signUp) {
        if (userRepository.existsByEmail(signUp.getEmail())) {
            throw new RuntimeException("이미 가입된 이메일입니다.");
        }
        String encryptedPassword = passwordEncoder.encode(signUp.getPassword());
        User user = User.builder()
                .email(signUp.getEmail())
                .password(encryptedPassword)
                .build();
        userRepository.save(user);
        return user;
    }
}
