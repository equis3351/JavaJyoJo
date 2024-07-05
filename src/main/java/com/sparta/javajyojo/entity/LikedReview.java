package com.sparta.javajyojo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "like_reviews")
public class LikedReview extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long likeReviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Column(nullable = false)
    private boolean liked = false;

    public LikedReview(User user, Review review) {
        this.user = user;
        this.review = review;
        this.liked = false;
    }

    public void updateLiked() {
        this.liked = !this.liked;
    }

}
