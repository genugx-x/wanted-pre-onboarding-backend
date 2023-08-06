package com.genug.wpob.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostEdit {

    private Long id;
    private String title;
    private String content;

    @Builder
    public PostEdit(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
