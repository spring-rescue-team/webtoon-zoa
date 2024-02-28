package com.project.webtoonzoa.repository;

import com.project.webtoonzoa.entity.Webtoon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WebtoonRepository extends JpaRepository<Webtoon, Long> {

    List<Webtoon> findAllByDeletedAtIsNull();

    List<Webtoon> findTop5ByOrderByLikesDesc();

    @Query("SELECT COUNT(wl) "
        + "FROM WebtoonLikes wl "
        + "WHERE wl.webtoon.id = :webtoonId")
    Long countLikesByWebtoonId(Long webtoonId);
}

