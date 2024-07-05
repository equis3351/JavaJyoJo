package com.sparta.javajyojo.repository.likedReview;

import com.sparta.javajyojo.entity.LikedReview;
import com.sparta.javajyojo.entity.Review;
import com.sparta.javajyojo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikedReviewRepository extends JpaRepository<LikedReview, Long>, LikedReviewRepositoryQuery{
    Optional<LikedReview> findByUserAndReview(User user, Review review);
}
