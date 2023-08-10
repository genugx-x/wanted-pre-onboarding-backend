package com.genug.wpob.api.repository;

import com.genug.wpob.api.domain.Post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getList(int size, int page);
}
