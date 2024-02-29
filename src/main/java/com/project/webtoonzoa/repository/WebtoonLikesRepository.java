package com.project.webtoonzoa.repository;

import com.project.webtoonzoa.entity.User;
import com.project.webtoonzoa.entity.Webtoon;
import com.project.webtoonzoa.entity.WebtoonLikes;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebtoonLikesRepository extends JpaRepository<WebtoonLikes, Long> {

    boolean existsByUserAndWebtoon(User user, Webtoon webtoon);

    Optional<WebtoonLikes> findByUserIdAndWebtoonId(Long userId, Long webtoonLikesId);
}
