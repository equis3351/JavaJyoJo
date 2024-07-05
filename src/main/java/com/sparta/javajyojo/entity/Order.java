package com.sparta.javajyojo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.javajyojo.enums.ErrorType;
import com.sparta.javajyojo.enums.OrderStatus;
import com.sparta.javajyojo.exception.CustomException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class) // JPA Auditing 활성화
public class Order extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnore // 순환 참조 방지를 위해 추가
    private List<OrderDetail> orderDetails;

    @Column(nullable = false)
    private String deliveryRequest;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    private String address;

    @Column(nullable = false)
    private Long likesCnt = 0L;

    @Column(nullable = false)
    private int totalPrice;

    public Order(User user, String deliveryRequest, String address, OrderStatus orderStatus, int totalPrice) {
        this.user = user;
        this.deliveryRequest = deliveryRequest;
        this.address = address;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
    }

    public void update(String deliveryRequest, String address, int totalPrice) {
        this.deliveryRequest = deliveryRequest;
        this.address = address;
        this.totalPrice = totalPrice;
    }

    public void delete() {
        // 주문 상태 확인 및 예외 처리
        switch (orderStatus) {
            case PROCESSING:
                throw new CustomException(ErrorType.ORDER_CANNOT_BE_CANCELED_PROCESSING);
            case COMPLETED:
                throw new CustomException(ErrorType.ORDER_CANNOT_BE_CANCELED_COMPLETED);
            case CANCELED:
                throw new CustomException(ErrorType.ORDER_CANNOT_BE_DELETED_CANCELED);
            default:
                // 주문 상태를 CANCELED 로 변경
                orderStatus = OrderStatus.CANCELED;
        }
    }

    public Long updateLike(boolean isLike) {
        if(isLike) {
            this.likesCnt += 1;
        } else {
            this.likesCnt -= 1;
        }
        return this.likesCnt;
    }

}
