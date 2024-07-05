package com.sparta.javajyojo.repository;

import com.sparta.javajyojo.entity.Follow;
import com.sparta.javajyojo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
}
