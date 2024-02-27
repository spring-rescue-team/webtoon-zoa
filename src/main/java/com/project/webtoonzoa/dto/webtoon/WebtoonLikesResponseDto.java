package com.project.webtoonzoa.dto.webtoon;

import com.project.webtoonzoa.entity.WebtoonLikes;
import lombok.Getter;

@Getter
public class WebtoonLikesResponseDto {

    private Long id;

    public WebtoonLikesResponseDto(WebtoonLikes webtoonLikes) {
        this.id = webtoonLikes.getId();
    }
}
