package com.genug.wpob.api.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PostsResponse {
    private final Long totalPostCount; // 전체 게시글 수
    private final Integer totalPageCount; // 전체 페이지 수
    private final List<PostResponse> posts; // 조회한 글 목록

    @Builder
    public PostsResponse(Long totalPostCount, Integer totalPageCount, List<PostResponse> posts) {
        this.totalPostCount = totalPostCount;
        this.totalPageCount = totalPageCount;
        this.posts = posts;
    }
}
