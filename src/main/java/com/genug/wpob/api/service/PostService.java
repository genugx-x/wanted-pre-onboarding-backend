package com.genug.wpob.api.service;

import com.genug.wpob.api.domain.Post;
import com.genug.wpob.api.domain.User;
import com.genug.wpob.api.exception.PostNotFoundException;
import com.genug.wpob.api.repository.PostRepository;
import com.genug.wpob.api.repository.UserRepository;
import com.genug.wpob.api.request.PostCreate;
import com.genug.wpob.api.response.PostResponse;
import com.genug.wpob.api.response.PostsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userSerivce;

    public void create(final Long userId, final PostCreate postCreate) {
        User user = userSerivce.findById(userId);
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

    public PostResponse get(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

}
