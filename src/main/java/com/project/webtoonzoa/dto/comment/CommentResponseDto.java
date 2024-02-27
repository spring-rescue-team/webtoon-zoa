package com.project.webtoonzoa.dto.comment;

import com.project.webtoonzoa.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CommentResponseDto {

    private Long id;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
    }
}
