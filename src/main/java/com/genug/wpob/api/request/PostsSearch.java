package com.genug.wpob.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static java.lang.Math.max;

@Getter
@Setter
public class PostsSearch {

    private Integer page = 1;
    private Integer size;

    @Builder
    public PostsSearch(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    public long getOffset() {
        return (long) (max(1, this.page) - 1) * size;
    }

}
