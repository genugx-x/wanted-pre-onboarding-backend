package com.genug.wpob.api.service;

import com.genug.wpob.api.domain.Post;
import com.genug.wpob.api.domain.User;
import com.genug.wpob.api.exception.AuthorizationException;
import com.genug.wpob.api.exception.PostNotFoundException;
import com.genug.wpob.api.repository.PostRepository;
import com.genug.wpob.api.repository.UserRepository;
import com.genug.wpob.api.request.PostCreate;
import com.genug.wpob.api.request.PostEdit;
import com.genug.wpob.api.response.PostResponse;
import com.genug.wpob.api.response.PostsResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import java.beans.Transient;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public void create(final Long userId, final PostCreate postCreate) {
        User user = userService.findById(userId);
        Post post = Post.builder()
                .user(user)
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();
        postRepository.save(post);
    }

    public PostsResponse getList(int size, int page) {
        long totalPostCount = postRepository.count();
        int totalPageCount = countTotalPage(size, totalPostCount);
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

    @Transactional
    public void edit(Long userId, Long postId, PostEdit postEdit) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        if (!userId.equals(post.getUser().getId())) { // 요청자와 작성자가 다른 경우
            throw new AuthorizationException();
        }
        post.edit(postEdit.getTitle(), postEdit.getContent());
    }

    public void delete(Long userId, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        if (!userId.equals(post.getUser().getId())) { // 요청자와 작성자가 다른 경우
            throw new AuthorizationException();
        }
        postRepository.delete(post);
    }

    private int countTotalPage(int size, long totalPostCount) {
        int totalPageCount = 0;
        if (totalPostCount > size) {
            totalPageCount += (int) totalPostCount / size;
            totalPageCount += totalPostCount % size > 0 ? 1 : 0;
        } else {
            totalPageCount++;
        }
        return totalPageCount;
    }

}
