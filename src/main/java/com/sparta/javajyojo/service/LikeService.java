package com.sparta.javajyojo.service;

import com.sparta.javajyojo.dto.LikeResponseDto;
import com.sparta.javajyojo.dto.LikedOrderResponseDto;
import com.sparta.javajyojo.dto.ReviewResponseDto;
import com.sparta.javajyojo.entity.*;
import com.sparta.javajyojo.enums.ContentTypeEnum;
import com.sparta.javajyojo.enums.ErrorType;
import com.sparta.javajyojo.exception.CustomException;
import com.sparta.javajyojo.repository.OrderRepository;
import com.sparta.javajyojo.repository.ReviewRepository;
import com.sparta.javajyojo.repository.likedOrder.LikedOrderRepository;
import com.sparta.javajyojo.repository.likedReview.LikedReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikedOrderRepository likedOrderRepository;
    private final LikedReviewRepository likedReviewRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public LikeResponseDto updateContentLike(ContentTypeEnum contentType, Long contentId, User user) {
        switch (contentType) {
            case ORDER:
                return updateOrderLike(contentId, user);
            case REVIEW:
                return updateReviewLike(contentId, user);
            default:
                throw new IllegalArgumentException("Unsupported content type: " + contentType);
        }
    }

    private LikeResponseDto updateOrderLike(Long contentId, User user) {

        Order order = getValidatedOrder(contentId);

        if (user.getUsername().equals(order.getUser().getUsername())) {
            throw new CustomException(ErrorType.CONTENT_OWNER);
        }

        LikedOrder likedOrder = likedOrderRepository.findByUserAndOrder(user, order)
                .orElseGet(() -> new LikedOrder(user, order));

        likedOrder.updateLiked();

        likedOrderRepository.save(likedOrder);

        return calculateOrderLike(likedOrder, order);
    }

    private LikeResponseDto updateReviewLike(Long contentId, User user) {

        Review review = getValidatedReview(contentId);

        if (user.getUsername().equals(review.getOrder().getUser().getUsername())) {
            throw new CustomException(ErrorType.CONTENT_OWNER);
        }

        LikedReview likedReview = likedReviewRepository.findByUserAndReview(user, review)
                .orElseGet(() -> new LikedReview(user, review));

        likedReview.updateLiked();
        likedReviewRepository.save(likedReview);

        return calculateReviewLike(likedReview, review);
    }

    public Page<LikedOrderResponseDto> getLikedOrders(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        int limit = pageable.getPageSize();

        List<Order> likedRestaurants = likedOrderRepository.findLikedOrdersByUser(user.getUserId(), limit);

        return new PageImpl<>(likedRestaurants.stream()
                .map(LikedOrderResponseDto::new)
                .collect(Collectors.toList()));
    }

    public Page<ReviewResponseDto> getLikedReviews(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        int limit = pageable.getPageSize();

        List<Review> likedReviews = likedReviewRepository.findLikedReviewsByUser(user.getUserId(), limit);

        return new PageImpl<>(likedReviews.stream()
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList()));
    }

    public Order getValidatedOrder(Long restaurantId){
        return orderRepository.findById(restaurantId).orElseThrow(() ->
                new CustomException(ErrorType.NOT_FOUND_ORDER));
    }

    public Review getValidatedReview(Long commentId){
        return reviewRepository.findById(commentId).orElseThrow(() ->
                new CustomException(ErrorType.NOT_FOUND_REVIEW));
    }

    private LikeResponseDto calculateOrderLike(LikedOrder likedOrder, Order order) {
        Long cnt =  order.updateLike(likedOrder.isLiked());
        return new LikeResponseDto(likedOrder.isLiked(), cnt);
    }

    public LikeResponseDto calculateReviewLike(LikedReview likedReview, Review review) {
        Long cnt =  review.updateLike(likedReview.isLiked());
        return new LikeResponseDto(likedReview.isLiked(), cnt);
    }

}
