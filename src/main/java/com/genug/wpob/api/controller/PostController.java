package com.genug.wpob.api.controller;

import com.genug.wpob.api.request.PostCreate;
import com.genug.wpob.api.request.PostEdit;
import com.genug.wpob.api.response.PostResponse;
import com.genug.wpob.api.response.PostsResponse;
import com.genug.wpob.api.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{postId}")
    public ResponseEntity<?> get(@PathVariable Long postId) {
        log.info("[PostController] get --- called");
        PostResponse response = postService.get(postId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping
    public ResponseEntity<?> edit(HttpServletRequest request, @RequestBody PostEdit postEdit) {
        log.info("[PostController] edit --- called");
        Long userId = (Long) request.getAttribute("userId");
        postService.edit(userId, postEdit);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable Long postId) {
        log.info("[PostController] delete --- called");
        Long userId = (Long) request.getAttribute("userId");
        postService.delete(userId, postId);
        return ResponseEntity.ok().build();
    }

}
