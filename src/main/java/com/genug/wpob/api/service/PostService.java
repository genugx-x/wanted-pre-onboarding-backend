package com.genug.wpob.api.service;

import com.genug.wpob.api.domain.Post;
import com.genug.wpob.api.domain.User;
import com.genug.wpob.api.repository.PostRepository;
import com.genug.wpob.api.repository.UserRepository;
import com.genug.wpob.api.request.PostCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void create(final Long userId, final PostCreate postCreate) {
        User user = userRepository.findById(userId).orElseThrow(RuntimeException::new);
        Post post = Post.builder()
                .user(user)
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();
        postRepository.save(post);
    }

}
