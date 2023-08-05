package com.genug.wpob.api.repository;

import com.genug.wpob.api.domain.Post;
import com.genug.wpob.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getList(int size, int page);
}
