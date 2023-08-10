package com.genug.wpob.api.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostSearch {
    private Long id;
    private String title;
    private String content;

    @Builder
    public PostSearch(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
