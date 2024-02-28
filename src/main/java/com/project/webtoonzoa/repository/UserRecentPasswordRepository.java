package com.project.webtoonzoa.repository;

import com.project.webtoonzoa.entity.UserRecentPassword;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRecentPasswordRepository extends JpaRepository<UserRecentPassword, Long> {

    List<UserRecentPassword> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}
