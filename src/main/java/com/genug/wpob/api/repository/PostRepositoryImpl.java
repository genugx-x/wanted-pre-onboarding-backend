package com.genug.wpob.api.repository;

import com.genug.wpob.api.domain.Post;
import com.genug.wpob.api.domain.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static java.lang.Math.max;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(int size, int page) {
        return jpaQueryFactory.selectFrom(QPost.post)
                .limit(size)
                .offset(getOffset(size, page))
                .orderBy(QPost.post.id.desc())
                .fetch();
    }

    private long getOffset(int size, int page) {
        return (long) (max(1, page) - 1) * size;
    }
}
