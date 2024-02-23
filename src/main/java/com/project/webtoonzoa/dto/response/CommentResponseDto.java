package com.project.webtoonzoa.dto.response;

import com.project.webtoonzoa.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private Long id;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
    }
}
