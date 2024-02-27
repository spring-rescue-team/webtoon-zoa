package com.project.webtoonzoa.dto.webtoon;

import com.project.webtoonzoa.entity.Enum.Category;
import com.project.webtoonzoa.entity.Enum.Day;
import lombok.Getter;

@Getter
public class WebtoonTop5ResponseDto {

    private Long id;
    private String title;
    private String description;
    private Category category;
    private String author;
    private Day day;
    private Long likes;

    public WebtoonTop5ResponseDto(Long id, String title, String description, Category category,
        String author, Day day, Long likes) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.author = author;
        this.day = day;
        this.likes = likes;
    }
}
