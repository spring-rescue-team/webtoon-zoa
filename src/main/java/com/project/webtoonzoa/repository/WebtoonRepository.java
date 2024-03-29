package com.project.webtoonzoa.repository;

import com.project.webtoonzoa.entity.Webtoon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebtoonRepository extends JpaRepository<Webtoon, Long> {

    List<Webtoon> findAllByDeletedAtIsNull();

    List<Webtoon> findTop5ByOrderByLikesDesc();
}

