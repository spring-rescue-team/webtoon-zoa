package com.project.webtoonzoa.repository;

import com.project.webtoonzoa.entity.Follow;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerIdAndFollowingId(Long follwerId, Long followingId);

    Optional<Follow> findByFollowerIdAndFollowingId(Long follwerId, Long followingId);
}
