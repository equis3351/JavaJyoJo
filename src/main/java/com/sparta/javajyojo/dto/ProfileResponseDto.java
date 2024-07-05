package com.sparta.javajyojo.dto;

import com.sparta.javajyojo.entity.User;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class ProfileResponseDto {
    private final String username;
    @Getter(value = AccessLevel.PRIVATE)
    private final String password;
    private final String name;
    private final String intro;
    private final String role;
    private long ordersLikedCnt;
    private long reviewsLikedCnt;

    public ProfileResponseDto(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.name = user.getName();
        this.intro = user.getIntro();
        this.role = String.valueOf(user.getRole());
    }

    public void updateContentLike(Long ordersLikedCnt, Long reviewsLikedCnt) {
        this.ordersLikedCnt = ordersLikedCnt;
        this.reviewsLikedCnt = reviewsLikedCnt;
    }

}
