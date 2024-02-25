package com.project.webtoonzoa.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class CommentRequestDto {

    private String content;
}
