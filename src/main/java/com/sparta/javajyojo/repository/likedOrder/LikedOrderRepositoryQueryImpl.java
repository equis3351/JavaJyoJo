package com.sparta.javajyojo.repository.likedOrder;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.javajyojo.entity.Review;
import com.sparta.javajyojo.repository.likedReview.LikedReviewRepositoryQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikedOrderRepositoryQueryImpl implements LikedReviewRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Review> findLikedReviewsByUser(Long id, int limit) {
        return List.of();
    }

    @Override
    public long countReviewLikesByUserId(Long id) {
        return 0;
    }
}
