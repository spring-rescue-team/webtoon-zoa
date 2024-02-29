package com.project.webtoonzoa.controller;

import com.project.webtoonzoa.dto.FollowResponseDto;
import com.project.webtoonzoa.global.response.CommonResponse;
import com.project.webtoonzoa.global.util.UserDetailsImpl;
import com.project.webtoonzoa.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("{followingId}")
    public ResponseEntity<CommonResponse<FollowResponseDto>> createFollow(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable(name = "followingId") Long followingId) {
        return ResponseEntity.status(HttpStatus.OK).body(
            CommonResponse.<FollowResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("팔로우 성공")
                .data(followService.createFollow(userDetails.getUser(), followingId))
                .build()
        );
    }

    @DeleteMapping("{followingId}")
    public ResponseEntity<CommonResponse<FollowResponseDto>> deleteFollow(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable(name = "followingId") Long followingId) {
        return ResponseEntity.status(HttpStatus.OK).body(
            CommonResponse.<FollowResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("팔로우 취소")
                .data(followService.deleteFollow(userDetails.getUser(), followingId))
                .build()
        );
    }
}
