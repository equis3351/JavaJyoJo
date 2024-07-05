package com.sparta.javajyojo.dto;

import com.sparta.javajyojo.entity.Order;
import lombok.Getter;

@Getter
public class LikedOrderResponseDto {
    private final Long orderId;
    private final Long userId;
    private final String deliveryRequest;
    private final String address;
    private final String orderStatus;
    private final int totalPrice;
    private final Long likesCnt;

    public LikedOrderResponseDto(Order order) {
        this.orderId = order.getOrderId();
        this.userId = order.getUser().getUserId();
        this.deliveryRequest = order.getDeliveryRequest();
        this.address = order.getAddress();
        this.orderStatus = order.getOrderStatus().name();
        this.totalPrice = order.getTotalPrice();
        this.likesCnt = order.getLikesCnt();
    }
}
