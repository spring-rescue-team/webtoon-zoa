package com.project.webtoonzoa.repository;

import com.project.webtoonzoa.dto.webtoon.WebtoonTop5ResponseDto;
import com.project.webtoonzoa.entity.Webtoon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WebtoonRepository extends JpaRepository<Webtoon, Long> {
    List<Webtoon> findAllByDeletedAtIsNull();

    @Query("SELECT NEW com.project.webtoonzoa.dto.webtoon.WebtoonTop5ResponseDto(w.id, w.title, w.description, w.category, w.author, w.day, COUNT(wl.webtoon.id)) " +
        "FROM Webtoon w " +
        "JOIN WebtoonLikes wl ON w.id = wl.webtoon.id " +
        "GROUP BY w.id, w.title, w.description, w.category, w.author, w.day " +
        "ORDER BY COUNT(wl.webtoon.id) DESC")
    List<WebtoonTop5ResponseDto> findTop5ByOrderByWebtoonLikesDesc();
}

