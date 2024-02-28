package com.project.webtoonzoa.repository;

import com.project.webtoonzoa.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT * FROM comment WHERE webtoon_id = :webtoon_id ORDER BY created_at ASC", nativeQuery = true)
    List<Comment> findAllCommentByWebtoonId(@Param("webtoon_id") Long webtoonId);
}
