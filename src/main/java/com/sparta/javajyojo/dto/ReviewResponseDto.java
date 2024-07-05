package com.sparta.javajyojo.dto;

import com.sparta.javajyojo.entity.Review;
import lombok.Getter;

@Getter
public class ReviewResponseDto {
    private final Long id;
    private final Long orderId;
    private final Long userId;
    private final String review;
    private final Long rating;
    private final Long likesCnt;

    public ReviewResponseDto(Review review) {
        this.id = review.getReviewId();
        this.orderId = review.getOrder().getOrderId(); // 쿼리는 2번 날라가게 됨
        this.userId = review.getUserId();
        this.review = review.getReview();
        this.rating = review.getRating();
        this.likesCnt = review.getLikesCnt();
    }
}
