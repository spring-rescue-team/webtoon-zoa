package com.project.webtoonzoa.dto;

import com.project.webtoonzoa.entity.Follow;
import lombok.Getter;

@Getter
public class FollowResponseDto {

    private Long id;

    public FollowResponseDto(Follow follow) {
        this.id = follow.getId();
    }
}
