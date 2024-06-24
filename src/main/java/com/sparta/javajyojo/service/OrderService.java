package com.sparta.javajyojo.service;

import com.sparta.javajyojo.dto.OrderRequestDto;
import com.sparta.javajyojo.dto.OrderResponseDto;
import com.sparta.javajyojo.entity.Order;
import com.sparta.javajyojo.entity.OrderDetail;
import com.sparta.javajyojo.entity.User;
import com.sparta.javajyojo.enums.ErrorType;
import com.sparta.javajyojo.enums.OrderStatus;
import com.sparta.javajyojo.enums.UserRoleEnum;
import com.sparta.javajyojo.exception.CustomException;
import com.sparta.javajyojo.repository.MenuRepository;
import com.sparta.javajyojo.repository.OrderDetailRepository;
import com.sparta.javajyojo.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderDetailRepository orderDetailRepository;

    // 주문 생성 메서드
    @Transactional
    public OrderResponseDto createOrder(User user, OrderRequestDto requestDto) {
        // 주문 총 금액 계산
        int totalPrice = calculateTotalPrice(requestDto);

        // 주문 엔티티 생성 및 저장
        Order order = new Order(user, requestDto.getDeliveryRequest(), requestDto.getAddress(), OrderStatus.NEW, totalPrice);
        orderRepository.save(order);

        // 주문 상세 내역 저장
        List<OrderDetail> orderDetails = saveOrderDetails(requestDto, order);

        return new OrderResponseDto(order, orderDetails);
    }

    // 주문 목록 조회 메서드
    public Page<OrderResponseDto> getOrders(User user, int page, int size) {
        Page<Order> orders;

        if (user.getRole() == UserRoleEnum.ADMIN) {
            orders = orderRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        } else {
            orders = orderRepository.findAllByUser(user, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        }

        return orders.map(order -> new OrderResponseDto(order, order.getOrderDetails()));
    }

    // 주문 조회 메서드
    public OrderResponseDto getOrder(User user, Long orderId) {
        // 주문 조회
        Order order = getOrderEntity(user, orderId);
        return new OrderResponseDto(order, order.getOrderDetails());
    }

    // 주문 업데이트 메서드
    @Transactional
    public OrderResponseDto updateOrder(User user, Long orderId, OrderRequestDto orderRequestDto) {
        log.info("주문 ID {}를 업데이트합니다.", orderId);

        // 주문 엔티티 조회
        Order order = getOrderEntity(user, orderId);

        if (user.getRole() == UserRoleEnum.ADMIN) {
            // 배송 요청 및 주소 업데이트
            order.setDeliveryRequest(orderRequestDto.getDeliveryRequest());
            order.setAddress(orderRequestDto.getAddress());

            // 주문 총 금액 재계산
            int totalPrice = calculateTotalPrice(orderRequestDto);
            order.setTotalPrice(totalPrice);
        } else {
            order.setDeliveryRequest(orderRequestDto.getDeliveryRequest());
            order.setAddress(orderRequestDto.getAddress());
        }

        // 업데이트된 주문 저장
        orderRepository.save(order);

        // 업데이트된 주문 상세 내역 저장
        List<OrderDetail> orderDetails = saveOrderDetails(orderRequestDto, order);

        return new OrderResponseDto(order, orderDetails);
    }

    // 관리자 전용 주문 상태 업데이트 메서드
    @Transactional
    public OrderResponseDto updateOrderStatus(User user, Long orderId, OrderStatus orderStatus) {
        if (user.getRole() != UserRoleEnum.ADMIN) {
            throw new CustomException(ErrorType.UNAUTHORIZED_ACCESS);
        }

        // 주문 엔티티 조회
        Order order = getOrderEntity(user, orderId);

        // 주문 상태 업데이트
        if (orderStatus != null) {
            order.setOrderStatus(orderStatus);
        } else {
            throw new CustomException(ErrorType.BAD_REQUEST);
        }

        // 업데이트된 주문 저장
        orderRepository.save(order);

        return new OrderResponseDto(order, order.getOrderDetails());
    }

    // 주문 삭제 메서드
    @Transactional
    public void deleteOrder(User user, Long orderId) {
        // 주문 엔티티 조회
        Order order = getOrderEntity(user, orderId);

        if (user.getRole() != UserRoleEnum.ADMIN && order.getUser().getUserId() != user.getUserId()) {
            throw new CustomException(ErrorType.UNAUTHORIZED_ACCESS);
        }

        // 주문 삭제
        orderRepository.delete(order);
    }

    //  주문 엔티티 조회 메서드
    private Order getOrderEntity(User user, Long orderId) {
        // 주문 조회
        Order order = orderRepository.findByOrderId(orderId);

        // 주문이 존재하지 않거나 사용자 권한이 없는 경우 예외 처리
        if (order == null || (user.getRole() != UserRoleEnum.ADMIN && order.getUser().getUserId() != user.getUserId())) {
            throw new CustomException(ErrorType.UNAUTHORIZED_ACCESS);
        }

        return order;
    }

    //  주문 총 금액 계산 메서드
    private int calculateTotalPrice(OrderRequestDto orderRequestDto) {
        // 주문 총 금액 계산
        int totalPrice = 0;
        List<OrderRequestDto.OrderDetailDto> orderDetails = orderRequestDto.getOrderDetails();
        if (orderDetails != null) {
            for (OrderRequestDto.OrderDetailDto detailDto : orderDetails) {
                int menuPrice = menuRepository.findById(detailDto.getMenuId())
                        .orElseThrow(() -> new CustomException(ErrorType.INVALID_INPUT))
                        .getPrice();
                totalPrice += menuPrice * detailDto.getAmount();
            }
        }
        return totalPrice;
    }

    //  주문 상세 내역 저장 메서드
    private List<OrderDetail> saveOrderDetails(OrderRequestDto orderRequestDto, Order order) {
        List<OrderDetail> orderDetails = orderRequestDto.getOrderDetails().stream()
                .map(detailDto -> {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrder(order);
                    orderDetail.setMenu(menuRepository.findById(detailDto.getMenuId())
                            .orElseThrow(() -> new CustomException(ErrorType.INVALID_INPUT)));
                    orderDetail.setAmount(detailDto.getAmount());
                    return orderDetailRepository.save(orderDetail);
                })
                .collect(Collectors.toList());

        return orderDetails;
    }
}