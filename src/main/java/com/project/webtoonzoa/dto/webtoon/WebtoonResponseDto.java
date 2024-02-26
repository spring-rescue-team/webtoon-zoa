package com.project.webtoonzoa.dto.webtoon;

import com.project.webtoonzoa.entity.Enum.Category;
import com.project.webtoonzoa.entity.Enum.Day;
import com.project.webtoonzoa.entity.Webtoon;
import lombok.Getter;

@Getter
public class WebtoonResponseDto {

    private Long id;
    private String title;
    private String description;
    private Category category;
    private String author;
    private Day day;

    public WebtoonResponseDto(Webtoon webtoon) {
        this.id = webtoon.getId();
        this.title = webtoon.getTitle();
        this.description = webtoon.getDescription();
        this.category = webtoon.getCategory();
        this.author = webtoon.getAuthor();
        this.day = webtoon.getDay();
    }
}
