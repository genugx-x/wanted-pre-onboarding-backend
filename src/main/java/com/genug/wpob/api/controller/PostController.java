package com.genug.wpob.api.controller;

import com.genug.wpob.api.request.PostCreate;
import com.genug.wpob.api.request.PostSearch;
import com.genug.wpob.api.response.PostResponse;
import com.genug.wpob.api.response.PostsResponse;
import com.genug.wpob.api.service.PostService;
import com.genug.wpob.security.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<?> create(HttpServletRequest request, @RequestBody PostCreate postCreate) {
        log.info("[PostController] create --- called");
        Long userId = (Long) request.getAttribute("userId");
        postService.create(userId, postCreate);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getList(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        log.info("[PostController] getList --- called");
        PostsResponse response = postService.getList(size, page);
        return ResponseEntity.ok(response);
    }


}
