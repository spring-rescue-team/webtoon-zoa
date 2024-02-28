package com.project.webtoonzoa.dto.comment;

import com.project.webtoonzoa.entity.Comment;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CommentDetailResponseDto {

    private Long id;
    private Long userId;
    private Long webtoonId;
    private String content;
    private List<CommentDetailResponseDto> children = new ArrayList<>();

    public CommentDetailResponseDto(Comment comment) {
        this.id = comment.getId();
        this.userId = comment.getUser().getId();
        this.webtoonId = comment.getWebtoon().getId();
        this.content = comment.getContent();
    }
}
