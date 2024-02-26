package com.project.webtoonzoa.entity;

import com.project.webtoonzoa.dto.webtoon.WebtoonRequestDto;
import com.project.webtoonzoa.entity.Enum.Category;
import com.project.webtoonzoa.entity.Enum.Day;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("deleted_at is NULL")
public class Webtoon extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "description")
    private String description;

    @Column(nullable = false, name = "category")
    @Enumerated(value = EnumType.STRING)
    private Category category;

    @Column(nullable = false, name = "author")
    private String author;

    @Column(nullable = false, name = "day")
    @Enumerated(value = EnumType.STRING)
    private Day day;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public Webtoon(WebtoonRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
        this.category = requestDto.getCategory();
        this.author = requestDto.getAuthor();
        this.day = requestDto.getDay();
    }

    public void update(WebtoonRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
        this.category = requestDto.getCategory();
        this.author = requestDto.getAuthor();
        this.day = requestDto.getDay();
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}
