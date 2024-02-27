package com.project.webtoonzoa.repository;

import com.project.webtoonzoa.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    List<Comment> findByWebtoonIdAndDeletedAtIsNullOrderByCreatedAtAsc(Long webtoonId);
}
