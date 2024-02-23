package com.project.webtoonzoa.controller;

import com.project.webtoonzoa.dto.CommonResponse;
import com.project.webtoonzoa.dto.request.CommentRequestDto;
import com.project.webtoonzoa.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommonResponse<Void>> createComment(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody CommentRequestDto requestDto) {
        commentService.createComment(userDetails.getUser(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(
            CommonResponse.<Void>builder()
                .status(HttpStatus.CREATED.value())
                .message("댓글 게시 완료")
                .build()
        );
    }
}
