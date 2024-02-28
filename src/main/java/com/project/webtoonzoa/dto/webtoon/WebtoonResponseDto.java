package com.project.webtoonzoa.dto.webtoon;

import com.project.webtoonzoa.entity.Enum.Category;
import com.project.webtoonzoa.entity.Enum.Day;
import com.project.webtoonzoa.entity.Webtoon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class WebtoonResponseDto {

    private Long webtoonId;
    private String title;
    private String description;
    private Category category;
    private String author;
    private Day day;
    private Long likes;

    public WebtoonResponseDto(Webtoon webtoon) {
        this.webtoonId = webtoon.getId();
        this.title = webtoon.getTitle();
        this.description = webtoon.getDescription();
        this.category = webtoon.getCategory();
        this.author = webtoon.getAuthor();
        this.day = webtoon.getDay();
        this.likes = webtoon.getLikes();
    }
}
