package com.sparta.javajyojo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "like_orders")
public class LikedOrder extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long likeOrderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private boolean liked = false;

    public LikedOrder(User user, Order order) {
        this.user = user;
        this.order = order;
        this.liked = false;
    }

    public void updateLiked() {
        this.liked = !this.liked;
    }

}
