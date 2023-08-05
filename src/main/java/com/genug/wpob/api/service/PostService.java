package com.genug.wpob.api.service;

import com.genug.wpob.api.domain.Post;
import com.genug.wpob.api.domain.User;
import com.genug.wpob.api.repository.PostRepository;
import com.genug.wpob.api.repository.UserRepository;
import com.genug.wpob.api.request.PostCreate;
import com.genug.wpob.api.request.PostSearch;
import com.genug.wpob.api.response.PostResponse;
import com.genug.wpob.api.response.PostsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public PostsResponse getList(int size, int page) {
        long totalPostCount = postRepository.count();
        int totalPageCount = 0;
        if (totalPostCount > size) {
            totalPageCount += (int) totalPostCount / size;
            totalPageCount += totalPostCount % size > 0 ? 1 : 0;
        } else {
            totalPageCount++;
        }
        return PostsResponse.builder()
                .posts(postRepository.getList(size, page)
                        .stream()
                        .map(post -> PostResponse.builder()
                                .id(post.getId())
                                .title(post.getTitle())
                                .build())
                        .toList())
                .totalPostCount(totalPostCount)
                .totalPageCount(totalPageCount)
                .build();
    }

}
