package com.project.webtoonzoa.dto.request;

import lombok.Getter;

@Getter
public class CommentRequestDto {

    private Long webtoonId;
    private String content;
}
