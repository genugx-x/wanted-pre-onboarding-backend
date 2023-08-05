package com.genug.wpob.api.controller;

import com.genug.wpob.api.domain.User;
import com.genug.wpob.api.request.Login;
import com.genug.wpob.api.request.Signup;
import com.genug.wpob.api.response.LoginResponse;
import com.genug.wpob.api.service.UserService;
import com.genug.wpob.security.TokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid Signup signup) {
        log.info("[UserController] signUp --- called");
        userService.create(signup);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid Login login) {
        log.info("[UserController] login --- called");
        User user = userService.verifyEmailAndPassword(login.getEmail(), login.getPassword());
        String token = tokenProvider.create(String.valueOf(user.getId()));
        return ResponseEntity.ok().body(new LoginResponse(token));
    }

}
