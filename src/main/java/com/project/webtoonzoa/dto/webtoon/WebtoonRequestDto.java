package com.project.webtoonzoa.dto.webtoon;

import com.project.webtoonzoa.entity.Enum.Category;
import com.project.webtoonzoa.entity.Enum.Day;
import lombok.Getter;

@Getter
public class WebtoonRequestDto {

    private String title;
    private String description;
    private Category category;
    private String author;
    private Day day;
}
