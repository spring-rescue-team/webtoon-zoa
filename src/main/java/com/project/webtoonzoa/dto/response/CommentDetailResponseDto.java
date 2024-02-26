package com.project.webtoonzoa.dto.response;

import com.project.webtoonzoa.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommentDetailResponseDto {

    private Long id;
    private Long userId;
    private Long webtoonId;
    private String content;

    public CommentDetailResponseDto(Comment savedComment, Long userId, Long webtoonId) {
        this.id = savedComment.getId();
        this.userId = userId;
        this.webtoonId = webtoonId;
        this.content = savedComment.getContent();
    }

    public CommentDetailResponseDto(Comment comment) {
        this.id = comment.getId();
        this.userId = comment.getUser().getId();
        this.webtoonId = comment.getWebtoon().getId();
        this.content = comment.getContent();
    }
}
