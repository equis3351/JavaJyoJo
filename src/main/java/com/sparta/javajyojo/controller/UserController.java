package com.sparta.javajyojo.controller;

import com.sparta.javajyojo.dto.ProfileRequestDto;
import com.sparta.javajyojo.dto.ProfileResponseDto;
import com.sparta.javajyojo.dto.SignUpRequestDto;
import com.sparta.javajyojo.security.UserDetailsImpl;
import com.sparta.javajyojo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("sign-up")
    public ResponseEntity<ProfileResponseDto> signUp(
            @Valid @RequestBody SignUpRequestDto requestDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signUp(requestDto));
    }

    @DeleteMapping("/sign-out")
    public ResponseEntity<String> signOut(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        userService.signOut(userDetails.getUser().getUserId());

        return ResponseEntity.ok().body("회원 탈퇴에 성공했습니다.");
    }

    @DeleteMapping("/log-out")
    public ResponseEntity<String> logOut(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        userService.logOut(userDetails.getUser().getUserId());

        return ResponseEntity.ok().body("로그아웃 성공하셨습니다");
    }

    @GetMapping
    public ResponseEntity<ProfileResponseDto> getProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok().body(userService.getProfile(userDetails.getUser().getUserId()));
    }

    @PatchMapping
    public ResponseEntity<ProfileResponseDto> profileUpdate(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody ProfileRequestDto requestDto) {

        return ResponseEntity.ok().body(userService.update(userDetails.getUser().getUserId(), requestDto));
    }

    @PostMapping("/{id}/following")
    public ResponseEntity<String> followUser(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        userService.followUser(id, userDetails.getUser());

        return ResponseEntity.ok().body("팔로우");
    }

    @DeleteMapping("/{id}/following")
    public ResponseEntity<String> unfollowUser(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        userService.unfollowUser(id, userDetails.getUser());

        return ResponseEntity.ok().body("언팔로우");
    }

}
