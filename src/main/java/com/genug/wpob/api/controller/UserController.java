package com.genug.wpob.api.controller;

import com.genug.wpob.api.request.SignUp;
import com.genug.wpob.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUp signUp) {
        log.info("[UserController] signUp --- called");
        userService.create(signUp);
        return ResponseEntity.ok().build();
    }

}
