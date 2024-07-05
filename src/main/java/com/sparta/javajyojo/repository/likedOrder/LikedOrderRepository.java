package com.sparta.javajyojo.repository.likedOrder;

import com.sparta.javajyojo.entity.LikedOrder;
import com.sparta.javajyojo.entity.Order;
import com.sparta.javajyojo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikedOrderRepository extends JpaRepository<LikedOrder, Long>, LikedOrderRepositoryQuery {
    Optional<LikedOrder> findByUserAndOrder(User user, Order order);
}
