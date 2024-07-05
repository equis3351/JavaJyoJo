package com.sparta.javajyojo.controller;

import com.sparta.javajyojo.dto.LikeResponseDto;
import com.sparta.javajyojo.dto.LikedOrderResponseDto;
import com.sparta.javajyojo.dto.ReviewResponseDto;
import com.sparta.javajyojo.enums.ContentTypeEnum;
import com.sparta.javajyojo.exception.CustomException;
import com.sparta.javajyojo.security.UserDetailsImpl;
import com.sparta.javajyojo.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PutMapping("/{contentType}/{contentId}/like")
    public ResponseEntity<String> updateRestaurantLike(
            @PathVariable("contentType") ContentTypeEnum contentType,
            @PathVariable("contentId") Long contentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws CustomException {

        LikeResponseDto likeResponseDto = likeService.updateContentLike(contentType, contentId, userDetails.getUser());

        boolean isLiked = likeResponseDto.isLiked();

        if (isLiked) {
            return ResponseEntity.ok().body("좋아요");
        } else {
            return ResponseEntity.ok().body("좋아요 취소");
        }

    }

    @GetMapping("/order/liked")
    public Page<LikedOrderResponseDto> getLikedRestaurants(
            @AuthenticationPrincipal UserDetailsImpl user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return likeService.getLikedOrders(user.getUser(), page, size);
    }

    @GetMapping("/review/liked")
    public Page<ReviewResponseDto> getLikedComments(
            @AuthenticationPrincipal UserDetailsImpl user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return likeService.getLikedReviews(user.getUser(), page, size);
    }

}
