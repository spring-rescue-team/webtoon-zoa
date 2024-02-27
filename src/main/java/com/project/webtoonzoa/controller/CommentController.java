package com.project.webtoonzoa.controller;

import com.project.webtoonzoa.dto.comment.CommentDetailResponseDto;
import com.project.webtoonzoa.dto.comment.CommentLikesResponseDto;
import com.project.webtoonzoa.dto.comment.CommentRequestDto;
import com.project.webtoonzoa.dto.comment.CommentResponseDto;
import com.project.webtoonzoa.global.response.CommonResponse;
import com.project.webtoonzoa.global.util.UserDetailsImpl;
import com.project.webtoonzoa.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments/{webtoonId}")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommonResponse<CommentResponseDto>> createComment(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable(name = "webtoonId") Long webtoonId,
        @RequestBody CommentRequestDto requestDto) {
        commentService.createComment(userDetails.getUser(), webtoonId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(
            CommonResponse.<CommentResponseDto>builder()
                .status(HttpStatus.CREATED.value())
                .message("댓글 게시 완료")
                .build()
        );
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<CommentDetailResponseDto>>> readComment(
        @PathVariable(name = "webtoonId") Long webtoonId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
            CommonResponse.<List<CommentDetailResponseDto>>builder()
                .status(HttpStatus.OK.value())
                .message("댓글 조회 성공")
                .data(commentService.readComment(webtoonId))
                .build()
        );
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommonResponse<CommentDetailResponseDto>> updateComment(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable(name = "webtoonId") Long webtoonId,
        @PathVariable(name = "commentId") Long commentId,
        @RequestBody CommentRequestDto requestDto
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
            CommonResponse.<CommentDetailResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("댓글 수정 완료")
                .data(commentService.updateComment(userDetails.getUser(), webtoonId, commentId,
                    requestDto))
                .build()
        );
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommonResponse<CommentResponseDto>> deleteComment(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable(name = "webtoonId") Long webtoonId,
        @PathVariable(name = "commentId") Long commentId) {
        return ResponseEntity.status(HttpStatus.OK).body(
            CommonResponse.<CommentResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("댓글 삭제 성공")
                .data(commentService.deleteComment(userDetails.getUser(), webtoonId, commentId))
                .build()
        );
    }

    @PostMapping("/{commentId}/likes")
    public ResponseEntity<CommonResponse<CommentLikesResponseDto>> createCommentLikes(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable(name = "commentId") Long commentId) {
        return ResponseEntity.status(HttpStatus.OK).body(
            CommonResponse.<CommentLikesResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("댓글 좋아요 성공")
                .data(commentService.createCommentLikes(userDetails.getUser(), commentId))
                .build()
        );
    }

    @DeleteMapping("/{commentId}/likes")
    public ResponseEntity<CommonResponse<CommentLikesResponseDto>> deleteCommentLikes(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable(name = "commentId") Long commentId) {
        return ResponseEntity.status(HttpStatus.OK).body(
            CommonResponse.<CommentLikesResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("댓글 좋아요 취소 성공")
                .data(commentService.deleteCommentLikes(userDetails.getUser(), commentId))
                .build()
        );
    }
}
