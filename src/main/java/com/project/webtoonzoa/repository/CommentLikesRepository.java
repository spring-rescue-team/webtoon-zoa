package com.project.webtoonzoa.repository;

import com.project.webtoonzoa.entity.Comment;
import com.project.webtoonzoa.entity.CommentLikes;
import com.project.webtoonzoa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikesRepository extends JpaRepository<CommentLikes, Long> {
    boolean existsByUserAndComment(User user, Comment comment);
}
