package com.project.webtoonzoa.dto.response;

import com.project.webtoonzoa.entity.CommentLikes;
import lombok.Getter;

@Getter
public class CommentLikesResponseDto {

    private Long id;

    public CommentLikesResponseDto(CommentLikes commentLikes) {
        this.id = commentLikes.getId();
    }
}
