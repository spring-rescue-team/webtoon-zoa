package com.project.webtoonzoa.controller;

import com.project.webtoonzoa.dto.CommonResponse;
import com.project.webtoonzoa.dto.request.CommentRequestDto;
import com.project.webtoonzoa.dto.response.CommentDetailResponseDto;
import com.project.webtoonzoa.dto.response.CommentLikesResponseDto;
import com.project.webtoonzoa.dto.response.CommentResponseDto;
import com.project.webtoonzoa.entity.User;
import com.project.webtoonzoa.global.util.UserDetailsImpl;
import com.project.webtoonzoa.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        commentService.createComment(new User(), webtoonId, requestDto);
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
                .data(commentService.updateComment(new User(), webtoonId, commentId, requestDto))
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
                .data(commentService.deleteComment(new User(), webtoonId, commentId))
                .build()
        );
    }

    @PostMapping("/{commentId}/likes")
    public ResponseEntity<CommonResponse<CommentLikesResponseDto>> createCommentLikes(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
             @PathVariable(name = "commentId") Long commentId){
        return ResponseEntity.status(HttpStatus.OK).body(
                CommonResponse.<CommentLikesResponseDto>builder()
                        .status(HttpStatus.OK.value())
                        .message("댓글 좋아요 성공")
                        .data(commentService.createCommentLikes(new User(), commentId))
                        .build()
        );
    }
    @DeleteMapping("/{commentId}/likes")
    public ResponseEntity<CommonResponse<CommentLikesResponseDto>> deleteCommentLikes(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable(name = "commentId") Long commentId){
        return ResponseEntity.status(HttpStatus.OK).body(
                CommonResponse.<CommentLikesResponseDto>builder()
                        .status(HttpStatus.OK.value())
                        .message("댓글 좋아요 취소 성공")
                        .data(commentService.deleteCommentLikes(new User(), commentId))
                        .build()
        );
    }
}